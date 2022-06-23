/*
 * This file is generated by jOOQ.
 */
package org.cooder.jooq.mate.db.tables.records;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.cooder.jooq.mate.db.tables.HouseLayout;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 户型就是房子的结构与形状，一般与设计方案相关，不同的设计会产出不同的户型，只要数据有调整，就会新生成一个户型
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class HouseLayoutRecord extends UpdatableRecordImpl<HouseLayoutRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>house_layout.id</code>. 主键id
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>house_layout.id</code>. 主键id
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>house_layout.house_layout_code</code>. 户型code
     */
    public void setHouseLayoutCode(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>house_layout.house_layout_code</code>. 户型code
     */
    public String getHouseLayoutCode() {
        return (String) get(1);
    }

    /**
     * Setter for <code>house_layout.from_house_layout_code</code>. 户型来源code，如果户型是由别的户型编辑而来，记录来源code
     */
    public void setFromHouseLayoutCode(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>house_layout.from_house_layout_code</code>. 户型来源code，如果户型是由别的户型编辑而来，记录来源code
     */
    public String getFromHouseLayoutCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>house_layout.living_room_num</code>. 客厅数量
     */
    public void setLivingRoomNum(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>house_layout.living_room_num</code>. 客厅数量
     */
    public Integer getLivingRoomNum() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>house_layout.bedroom_num</code>. 居室数量
     */
    public void setBedroomNum(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>house_layout.bedroom_num</code>. 居室数量
     */
    public Integer getBedroomNum() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>house_layout.kitchen_num</code>. 厨房数量
     */
    public void setKitchenNum(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>house_layout.kitchen_num</code>. 厨房数量
     */
    public Integer getKitchenNum() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>house_layout.toilet_num</code>. 卫生间数量
     */
    public void setToiletNum(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>house_layout.toilet_num</code>. 卫生间数量
     */
    public Integer getToiletNum() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>house_layout.dry_toilet_num</code>. 卫生间干区数量
     */
    public void setDryToiletNum(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>house_layout.dry_toilet_num</code>. 卫生间干区数量
     */
    public Integer getDryToiletNum() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>house_layout.balcony_num</code>. 阳台数量
     */
    public void setBalconyNum(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>house_layout.balcony_num</code>. 阳台数量
     */
    public Integer getBalconyNum() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>house_layout.storeroom_num</code>. 储物间数量
     */
    public void setStoreroomNum(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>house_layout.storeroom_num</code>. 储物间数量
     */
    public Integer getStoreroomNum() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>house_layout.inside_area_value</code>. 套内面积
     */
    public void setInsideAreaValue(BigDecimal value) {
        set(10, value);
    }

    /**
     * Getter for <code>house_layout.inside_area_value</code>. 套内面积
     */
    public BigDecimal getInsideAreaValue() {
        return (BigDecimal) get(10);
    }

    /**
     * Setter for <code>house_layout.inside_area_unit</code>. 套内面积单位
     */
    public void setInsideAreaUnit(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>house_layout.inside_area_unit</code>. 套内面积单位
     */
    public String getInsideAreaUnit() {
        return (String) get(11);
    }

    /**
     * Setter for <code>house_layout.house_area_value</code>. 房屋面积
     */
    public void setHouseAreaValue(BigDecimal value) {
        set(12, value);
    }

    /**
     * Getter for <code>house_layout.house_area_value</code>. 房屋面积
     */
    public BigDecimal getHouseAreaValue() {
        return (BigDecimal) get(12);
    }

    /**
     * Setter for <code>house_layout.house_area_unit</code>. 房屋面积单位
     */
    public void setHouseAreaUnit(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>house_layout.house_area_unit</code>. 房屋面积单位
     */
    public String getHouseAreaUnit() {
        return (String) get(13);
    }

    /**
     * Setter for <code>house_layout.wall_inside_area_value</code>. 外墙内框面积
     */
    public void setWallInsideAreaValue(BigDecimal value) {
        set(14, value);
    }

    /**
     * Getter for <code>house_layout.wall_inside_area_value</code>. 外墙内框面积
     */
    public BigDecimal getWallInsideAreaValue() {
        return (BigDecimal) get(14);
    }

    /**
     * Setter for <code>house_layout.wall_inside_area_unit</code>. 外墙内框面积单位
     */
    public void setWallInsideAreaUnit(String value) {
        set(15, value);
    }

    /**
     * Getter for <code>house_layout.wall_inside_area_unit</code>. 外墙内框面积单位
     */
    public String getWallInsideAreaUnit() {
        return (String) get(15);
    }

    /**
     * Setter for <code>house_layout.house_layout_file_url</code>. 户型数据详情（坐标json文件地址）
     */
    public void setHouseLayoutFileUrl(String value) {
        set(16, value);
    }

    /**
     * Getter for <code>house_layout.house_layout_file_url</code>. 户型数据详情（坐标json文件地址）
     */
    public String getHouseLayoutFileUrl() {
        return (String) get(16);
    }

    /**
     * Setter for <code>house_layout.house_layout_image</code>. 户型设计图文件地址
     */
    public void setHouseLayoutImage(String value) {
        set(17, value);
    }

    /**
     * Getter for <code>house_layout.house_layout_image</code>. 户型设计图文件地址
     */
    public String getHouseLayoutImage() {
        return (String) get(17);
    }

    /**
     * Setter for <code>house_layout.marked_house_layout_image</code>. 户型标注设计图文件地址
     */
    public void setMarkedHouseLayoutImage(String value) {
        set(18, value);
    }

    /**
     * Getter for <code>house_layout.marked_house_layout_image</code>. 户型标注设计图文件地址
     */
    public String getMarkedHouseLayoutImage() {
        return (String) get(18);
    }

    /**
     * Setter for <code>house_layout.status</code>. 户型数据状态，0-无效，1-草稿，2-正式
     */
    public void setStatus(Integer value) {
        set(19, value);
    }

    /**
     * Getter for <code>house_layout.status</code>. 户型数据状态，0-无效，1-草稿，2-正式
     */
    public Integer getStatus() {
        return (Integer) get(19);
    }

    /**
     * Setter for <code>house_layout.cu_name</code>. 创建人
     */
    public void setCuName(String value) {
        set(20, value);
    }

    /**
     * Getter for <code>house_layout.cu_name</code>. 创建人
     */
    public String getCuName() {
        return (String) get(20);
    }

    /**
     * Setter for <code>house_layout.cuid</code>. 创建人ucid
     */
    public void setCuid(Long value) {
        set(21, value);
    }

    /**
     * Getter for <code>house_layout.cuid</code>. 创建人ucid
     */
    public Long getCuid() {
        return (Long) get(21);
    }

    /**
     * Setter for <code>house_layout.mu_name</code>. 更新人
     */
    public void setMuName(String value) {
        set(22, value);
    }

    /**
     * Getter for <code>house_layout.mu_name</code>. 更新人
     */
    public String getMuName() {
        return (String) get(22);
    }

    /**
     * Setter for <code>house_layout.muid</code>. 更新人ucid
     */
    public void setMuid(Long value) {
        set(23, value);
    }

    /**
     * Getter for <code>house_layout.muid</code>. 更新人ucid
     */
    public Long getMuid() {
        return (Long) get(23);
    }

    /**
     * Setter for <code>house_layout.ctime</code>. 创建时间 
     */
    public void setCtime(LocalDateTime value) {
        set(24, value);
    }

    /**
     * Getter for <code>house_layout.ctime</code>. 创建时间 
     */
    public LocalDateTime getCtime() {
        return (LocalDateTime) get(24);
    }

    /**
     * Setter for <code>house_layout.mtime</code>. 更新时间
     */
    public void setMtime(LocalDateTime value) {
        set(25, value);
    }

    /**
     * Getter for <code>house_layout.mtime</code>. 更新时间
     */
    public LocalDateTime getMtime() {
        return (LocalDateTime) get(25);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached HouseLayoutRecord
     */
    public HouseLayoutRecord() {
        super(HouseLayout.HOUSE_LAYOUT);
    }

    /**
     * Create a detached, initialised HouseLayoutRecord
     */
    public HouseLayoutRecord(Long id, String houseLayoutCode, String fromHouseLayoutCode, Integer livingRoomNum, Integer bedroomNum, Integer kitchenNum, Integer toiletNum, Integer dryToiletNum, Integer balconyNum, Integer storeroomNum, BigDecimal insideAreaValue, String insideAreaUnit, BigDecimal houseAreaValue, String houseAreaUnit, BigDecimal wallInsideAreaValue, String wallInsideAreaUnit, String houseLayoutFileUrl, String houseLayoutImage, String markedHouseLayoutImage, Integer status, String cuName, Long cuid, String muName, Long muid, LocalDateTime ctime, LocalDateTime mtime) {
        super(HouseLayout.HOUSE_LAYOUT);

        setId(id);
        setHouseLayoutCode(houseLayoutCode);
        setFromHouseLayoutCode(fromHouseLayoutCode);
        setLivingRoomNum(livingRoomNum);
        setBedroomNum(bedroomNum);
        setKitchenNum(kitchenNum);
        setToiletNum(toiletNum);
        setDryToiletNum(dryToiletNum);
        setBalconyNum(balconyNum);
        setStoreroomNum(storeroomNum);
        setInsideAreaValue(insideAreaValue);
        setInsideAreaUnit(insideAreaUnit);
        setHouseAreaValue(houseAreaValue);
        setHouseAreaUnit(houseAreaUnit);
        setWallInsideAreaValue(wallInsideAreaValue);
        setWallInsideAreaUnit(wallInsideAreaUnit);
        setHouseLayoutFileUrl(houseLayoutFileUrl);
        setHouseLayoutImage(houseLayoutImage);
        setMarkedHouseLayoutImage(markedHouseLayoutImage);
        setStatus(status);
        setCuName(cuName);
        setCuid(cuid);
        setMuName(muName);
        setMuid(muid);
        setCtime(ctime);
        setMtime(mtime);
    }
}
