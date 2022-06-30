package org.cooder.jooq.mate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.lang.model.element.Modifier;

import org.cooder.jooq.mate.types.AbstractRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Repository;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

interface TableMeta {
    String getName();

    String getComment();

    FieldMeta[] fields();
}

interface FieldMeta {
    String getName();

    Class<?> getType();

    String getComment();

    default String getNameDesc() {
        return getComment();
    }

    default String getEnums() {
        return "";
    }
}

public interface Generator {
    String valueVar = "value";
    String nameVar = "name";

    void generate(TableMeta table);

    default boolean ignore(TypeGeneratorStrategy strategy, TableMeta table, FieldMeta field) {
        return strategy.isIgnoreField(table.getName(), field.getName());
    }

    default FieldMeta[] filterdFields(TypeGeneratorStrategy strategy, TableMeta table) {
        return Arrays.asList(table.fields())
                .stream()
                .filter(f -> !ignore(strategy, table, f))
                .collect(Collectors.toList())
                .toArray(new FieldMeta[0]);
    }

    default MethodSpec.Builder generateGetter(TableMeta table, FieldMeta field) {
        MethodSpec.Builder b = MethodSpec.methodBuilder("get" + StringUtils.toCamelCase(field.getName()))
                .addModifiers(Modifier.PUBLIC)
                .returns(field.getType())
                .addJavadoc("获取`" + field.getNameDesc() + "`");
        return b;
    }

    default void addSuppressWarnings(MethodSpec.Builder b) {
        b.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                .addMember(valueVar, "{$S, $S}", "unchecked", "rawtypes")
                .build());
    }

    default MethodSpec.Builder generateSetter(TableMeta table, FieldMeta field) {
        String nameLC = StringUtils.toCamelCaseLC(field.getName());
        MethodSpec.Builder b = MethodSpec.methodBuilder("set" + StringUtils.toCamelCase(field.getName()))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(field.getType(), nameLC)
                .returns(void.class)
                .addJavadoc("设置`" + field.getNameDesc() + "`");
        return b;
    }

    default void output(String indent, String directory, String packageName, TypeSpec typeSpec) {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .indent(indent)
                .skipJavaLangImports(true)
                .addFileComment("\nThis file is generated by JOOQ-MATE.\n")
                .build();
        try {
            javaFile.writeTo(new File(directory));
        } catch (IOException e) {
            throw new RuntimeException("failed to write file.", e);
        }
        System.out.println(String.format("generated: %s.%s", packageName, typeSpec.name));
    }
}

class TypeInterfaceGenerator implements Generator {
    private final TypeGeneratorStrategy strategy;

    public TypeInterfaceGenerator(TypeGeneratorStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void generate(TableMeta table) {
        String tableName = table.getName();
        if(!strategy.isGenerateInterface(tableName)) {
            return;
        }

        String clazzName = strategy.interfaceClazzName(tableName);
        TypeSpec.Builder ts = TypeSpec.interfaceBuilder(clazzName)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(table.getComment());

        ts.addSuperinterfaces(strategy.getGeneratedInterfaceSuperInterfaces(tableName));
        FieldMeta[] fields = filterdFields(strategy, table);

        Arrays.asList(fields).forEach(f -> {
            generateGetter(ts, table, f);
            generateEnumGetter(ts, f);
            generateSetter(ts, table, f);
        });

        Arrays.asList(fields).forEach(f -> generateInnerEnums(ts, table, f));

        output(strategy.getIndent(), strategy.getDirectory(), strategy.interfacePackageName(tableName), ts.build());
    }

    private void generateGetter(Builder ts, TableMeta table, FieldMeta f) {
        MethodSpec.Builder b = generateGetter(table, f);
        b.addModifiers(Modifier.ABSTRACT);
        ts.addMethod(b.build());
    }

    private void generateSetter(Builder ts, TableMeta table, FieldMeta f) {
        MethodSpec.Builder b = generateSetter(table, f);
        b.addModifiers(Modifier.ABSTRACT);
        ts.addMethod(b.build());
    }

    private void generateEnumGetter(Builder ts, FieldMeta f) {
        String enumString = f.getEnums();
        if(StringUtils.isEmpty(enumString)) {
            return;
        }

        String enumName = StringUtils.toCamelCase(f.getName());
        String methodName = String.format("get%sName", enumName);
        MethodSpec.Builder b = MethodSpec.methodBuilder(methodName)
                .addJavadoc("获取`" + f.getNameDesc() + "`名称")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .returns(String.class);

        b.addStatement("return $L.from(get$L()).name", enumName, enumName);

        ts.addMethod(b.build());
    }

    private void generateInnerEnums(Builder parent, TableMeta table, FieldMeta fm) {
        String enumString = fm.getEnums();
        if(StringUtils.isEmpty(enumString)) {
            return;
        }

        String tableName = table.getName();
        String enumName = StringUtils.toCamelCase(fm.getName());
        ClassName enumClassName = getInnerEnumClassName(tableName, enumName);

        TypeSpec.Builder ts = TypeSpec.enumBuilder(enumName).addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        String[] pairs = MateUtils.split(enumString, ",");
        for (String pair : pairs) {
            String[] ss = MateUtils.split(pair, ":");
            ts.addEnumConstant(ss[1], TypeSpec.anonymousClassBuilder("$L, $S", ss[0], ss[1]).build());
        }

        ts.addField(FieldSpec.builder(int.class, valueVar, Modifier.PUBLIC, Modifier.FINAL).build());
        ts.addField(FieldSpec.builder(String.class, nameVar, Modifier.PUBLIC, Modifier.FINAL).build());
        ts.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(int.class, valueVar)
                .addParameter(String.class, nameVar)
                .addStatement("this.$L = $L", valueVar, valueVar)
                .addStatement("this.$L = $L", nameVar, nameVar)
                .build());

        ts.addMethod(MethodSpec.methodBuilder("from")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(enumClassName)
                .addParameter(int.class, valueVar)
                .addCode(CodeBlock.builder()
                        .beginControlFlow("for ($T e : values())", enumClassName)
                        .addStatement("if ( e.value == value) return e")
                        .endControlFlow()
                        .addStatement("return null")
                        .build())
                .build());

        parent.addType(ts.build());
    }

    private ClassName getInnerEnumClassName(String tableName, String fieldName) {
        String name = StringUtils.toCamelCase(fieldName);
        return ClassName.get(strategy.interfacePackageName(tableName), strategy.interfaceClazzName(tableName), name);
    }

    public static TypeInterfaceGenerator of(TypeGeneratorStrategy strategy) {
        return new TypeInterfaceGenerator(strategy);
    }
}

class TypeRecordGenerator implements Generator {
    private final TypeGeneratorStrategy strategy;

    public TypeRecordGenerator(TypeGeneratorStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void generate(TableMeta table) {
        String tableName = table.getName();
        if(!strategy.isGenerateRecord(tableName)) {
            return;
        }

        String clazz = strategy.recordClazzName(tableName);
        ClassName genericType = strategy.pojoClassName(tableName);
        TypeSpec.Builder ts = TypeSpec.classBuilder(clazz)
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(ClassName.get(AbstractRecord.class), genericType))
                .addSuperinterface(strategy.interfaceClassName(tableName));

        FieldMeta[] fields = filterdFields(strategy, table);
        generateRecordFields(ts, fields);
        generateRecordConstructor(ts, genericType);

        for (int i = 0; i < fields.length; i++) {
            generateGetter(ts, table, fields[i], i);
            generateSetter(ts, table, fields[i], i);
        }

        output(strategy.getIndent(), strategy.getDirectory(), strategy.recordPackageName(tableName), ts.build());
    }

    private void generateRecordFields(TypeSpec.Builder ts, FieldMeta[] fields) {
        FieldSpec.Builder b = FieldSpec.builder(AbstractRecord.Field[].class, "FIELDS", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);
        com.squareup.javapoet.CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("new Field[] {\n");

        for (int i = 0; i < fields.length; i++) {
            String fieldName = StringUtils.toCamelCaseLC(fields[i].getName());
            cb.add(strategy.getIndent() + "$T.builder().name($S).type($T.class).desc($S).build(),\n", AbstractRecord.Field.class,
                    fieldName, fields[i].getType(), fields[i].getNameDesc());
        }
        cb.add("}");
        b.initializer(cb.build());
        ts.addField(b.build());
    }

    private void generateRecordConstructor(Builder ts, ClassName genericType) {
        MethodSpec.Builder b = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        b.addStatement("super($T.class, FIELDS)", genericType);
        ts.addMethod(b.build());
    }

    private void generateGetter(Builder ts, TableMeta table, FieldMeta field, int index) {
        MethodSpec.Builder b = generateGetter(table, field);
        b.addAnnotation(Override.class);
        b.addStatement("return ($T)get($L)", field.getType(), index);
        ts.addMethod(b.build());
    }

    private void generateSetter(Builder ts, TableMeta table, FieldMeta f, int index) {
        String nameLC = StringUtils.toCamelCaseLC(f.getName());
        MethodSpec.Builder b = generateSetter(table, f);
        b.addAnnotation(Override.class);
        b.addStatement("set($L, $N)", index, nameLC);
        ts.addMethod(b.build());
    }

    public static TypeRecordGenerator of(TypeGeneratorStrategy strategy) {
        return new TypeRecordGenerator(strategy);
    }
}

class TypePojoGenerator implements Generator {
    private final TypeGeneratorStrategy strategy;

    public TypePojoGenerator(TypeGeneratorStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void generate(TableMeta table) {
        String tableName = table.getName();
        if(!strategy.isGeneratePojo(tableName)) {
            return;
        }

        ClassName pojoCN = strategy.pojoClassName(tableName);
        TypeSpec.Builder ts = TypeSpec.classBuilder(pojoCN.simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(strategy.interfaceClassName(tableName));

        TypeName superClass = strategy.getPojoSuperClass(tableName);
        if(superClass != null) {
            ts.superclass(superClass);
        }
        FieldMeta[] fields = filterdFields(strategy, table);
        generatePojoFields(ts, fields);

        if(strategy.isGeneratePojoWithLombok(tableName)) {
            ts.addAnnotation(lombok.Data.class)
                    .addAnnotation(lombok.experimental.SuperBuilder.class)
                    .addAnnotation(lombok.NoArgsConstructor.class)
                    .addAnnotation(AnnotationSpec.builder(lombok.ToString.class).addMember("callSuper", "true").build());
            if(superClass != null) {
                ts.addAnnotation(AnnotationSpec.builder(lombok.EqualsAndHashCode.class).addMember("callSuper", "true").build());
            } else {
                ts.addAnnotation(lombok.EqualsAndHashCode.class);
            }
        } else {
            Arrays.asList(fields).forEach(f -> {
                generateGetter(ts, table, f);
                generateSetter(ts, table, f);
            });
        }

        output(strategy.getIndent(), strategy.getDirectory(), strategy.pojoPackageName(tableName), ts.build());

    }

    private void generatePojoFields(TypeSpec.Builder ts, FieldMeta[] fields) {
        for (int i = 0; i < fields.length; i++) {
            FieldMeta f = fields[i];
            String fieldName = StringUtils.toCamelCaseLC(f.getName());
            FieldSpec spec = FieldSpec.builder(f.getType(), fieldName, Modifier.PRIVATE)
                    .addJavadoc(f.getComment())
                    .build();
            ts.addField(spec);
        }
    }

    private void generateGetter(Builder ts, TableMeta table, FieldMeta field) {
        String nameLC = StringUtils.toCamelCaseLC(field.getName());
        MethodSpec.Builder b = generateGetter(table, field);
        b.addAnnotation(Override.class);
        b.addStatement("return this.$N", nameLC);
        ts.addMethod(b.build());
    }

    private void generateSetter(Builder ts, TableMeta table, FieldMeta f) {
        String nameLC = StringUtils.toCamelCaseLC(f.getName());
        MethodSpec.Builder b = generateSetter(table, f);
        b.addAnnotation(Override.class);
        b.addStatement("this.$N = $N", nameLC, nameLC);
        ts.addMethod(b.build());
    }

    public static TypePojoGenerator of(TypeGeneratorStrategy strategy) {
        return new TypePojoGenerator(strategy);
    }
}

class RepoGenerator implements Generator {

    private final TypeGeneratorStrategy strategy;

    public RepoGenerator(TypeGeneratorStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void generate(TableMeta table) {
        String tableName = table.getName();
        if(!strategy.isGenerateRepo(tableName)) {
            return;
        }

        ClassName repoCN = strategy.repoClassName(tableName);
        TypeSpec.Builder ts = TypeSpec.classBuilder(repoCN.simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Repository.class);

        ts.addField(FieldSpec.builder(DSLContext.class, "db", Modifier.PRIVATE)
                .addAnnotation(Resource.class)
                .build());

        generateCreater(ts, table);
        generateUpdater(ts, table);
        generateGetter(ts, table);
        generateLister(ts, table);

        output(strategy.getIndent(), strategy.getRepoDirectory(), strategy.repoPackageName(tableName), ts.build());
    }

    private void generateCreater(TypeSpec.Builder ts, TableMeta table) {
        MethodSpec.Builder b = generateMethod(table, "create");
        b.returns(void.class);

        b.addCode(CodeBlock.builder()
                .addStatement("rec.save()")
                .build());

        ts.addMethod(b.build());
    }

    private void generateUpdater(TypeSpec.Builder ts, TableMeta table) {
        MethodSpec.Builder b = generateMethod(table, "update");
        b.returns(void.class);

        b.addCode(CodeBlock.builder()
                .addStatement("rec.update()")
                .build());

        ts.addMethod(b.build());
    }

    private void generateGetter(TypeSpec.Builder ts, TableMeta table) {
        String tableName = table.getName();
        ClassName pojoCN = strategy.pojoClassName(tableName);

        MethodSpec.Builder b = generateMethod(table, "get");
        addSuppressWarnings(b);
        b.returns(pojoCN);

        b.addCode(CodeBlock.builder()
                .addStatement("SelectConditionStep sql = db.selectFrom(rec.getTable()).where($T.noCondition())", DSL.class)
                .addStatement("$T[] fields = rec.fields()", Field.class)
                .beginControlFlow("for ($T field : fields)", Field.class)
                .beginControlFlow("if (field.changed(rec))")
                .addStatement("sql = sql.and(field.eq(rec.get(field)))")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return sql.fetchOne().into($T.class)", pojoCN)
                .build());

        ts.addMethod(b.build());
    }

    private void generateLister(TypeSpec.Builder ts, TableMeta table) {
        String tableName = table.getName();
        ClassName pojoCN = strategy.pojoClassName(tableName);

        MethodSpec.Builder b = generateMethod(table, "list");
        addSuppressWarnings(b);
        b.returns(ParameterizedTypeName.get(ClassName.get(List.class), pojoCN));

        b.addCode(CodeBlock.builder()
                .addStatement("SelectConditionStep sql = db.selectFrom(rec.getTable()).where($T.noCondition())", DSL.class)
                .addStatement("$T[] fields = rec.fields()", Field.class)
                .beginControlFlow("for ($T field : fields)", Field.class)
                .beginControlFlow("if (field.changed(rec))")
                .addStatement("sql = sql.and(field.eq(rec.get(field)))")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return sql.fetch().into($T.class)", pojoCN)
                .build());

        ts.addMethod(b.build());
    }

    private MethodSpec.Builder generateMethod(TableMeta table, String type) {
        String tableName = table.getName();
        ClassName intrCN = strategy.interfaceClassName(tableName);
        ClassName pojoCN = strategy.pojoClassName(tableName);
        MethodSpec.Builder b = MethodSpec.methodBuilder(type + intrCN.simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(pojoCN, "entity");

        ClassName jooqRecordCN = strategy.jooqRecordClassName(tableName);
        b.addStatement("$T rec  = new $T()", jooqRecordCN, jooqRecordCN);
        b.addStatement("rec.from(entity)");
        return b;
    }

    public static RepoGenerator of(TypeGeneratorStrategy strategy) {
        return new RepoGenerator(strategy);
    }
}