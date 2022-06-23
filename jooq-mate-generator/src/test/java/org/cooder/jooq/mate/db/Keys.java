/*
 * This file is generated by jOOQ.
 */
package org.cooder.jooq.mate.db;


import org.cooder.jooq.mate.db.tables.HouseLayout;
import org.cooder.jooq.mate.db.tables.Space;
import org.cooder.jooq.mate.db.tables.records.HouseLayoutRecord;
import org.cooder.jooq.mate.db.tables.records.SpaceRecord;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * the default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {
    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<HouseLayoutRecord> KEY_HOUSE_LAYOUT_PRIMARY = Internal.createUniqueKey(HouseLayout.HOUSE_LAYOUT, DSL.name("KEY_house_layout_PRIMARY"), new TableField[] { HouseLayout.HOUSE_LAYOUT.ID }, true);
    public static final UniqueKey<HouseLayoutRecord> KEY_HOUSE_LAYOUT_UNIQ_IDX_CODE = Internal.createUniqueKey(HouseLayout.HOUSE_LAYOUT, DSL.name("KEY_house_layout_uniq_idx_code"), new TableField[] { HouseLayout.HOUSE_LAYOUT.HOUSE_LAYOUT_CODE }, true);
    public static final UniqueKey<SpaceRecord> KEY_SPACE_PRIMARY = Internal.createUniqueKey(Space.SPACE, DSL.name("KEY_space_PRIMARY"), new TableField[] { Space.SPACE.ID }, true);
    public static final UniqueKey<SpaceRecord> KEY_SPACE_UNIQ_IDX_SPACE_CODE = Internal.createUniqueKey(Space.SPACE, DSL.name("KEY_space_uniq_idx_space_code"), new TableField[] { Space.SPACE.SPACE_CODE, Space.SPACE.HOUSE_LAYOUT_CODE }, true);
}
