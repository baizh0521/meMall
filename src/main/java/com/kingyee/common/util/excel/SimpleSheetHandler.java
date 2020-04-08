package com.kingyee.common.util.excel;


import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.List;

/**
 * Created by zhl on 2018/11/13.
 */
public class SimpleSheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
    //读取后数据存放
    private List<String[]> datas;
    //读取多少列
    private int columnsLength;
    // 读取行信息
    private String[] readRow;

    /**
     * 初始化
     *
     * @param datas
     * @param columnsLength
     */
    public void init(List<String[]> datas, int columnsLength) {
        this.datas = datas;
        this.columnsLength = columnsLength;
    }

    /**
     * 开始读取一行
     *
     * @param rowNum
     */
    @Override
    public void startRow(int rowNum) {
        readRow = new String[columnsLength];
    }

    /**
     * 结束读取一行
     *
     * @param rowNum
     */
    @Override
    public void endRow(int rowNum) {
        datas.add(readRow.clone());
        readRow = null;
    }

    /**
     * 读取每个单元格
     * 注意,只有单元格有值才会调用此方法
     * 所以数据存储用数组,保证每行的数据长度一致,否则会出现某行有列值缺失时,造成的数据不能和列索引对应
     *
     * @param cellReference
     * @param formattedValue
     * @param comment
     */
    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
        //转换A1,B1,C1等表格位置为真实索引位置
        int index = getCellIndex(cellReference);
        if (index < columnsLength) {
            readRow[index] = formattedValue;
        }
    }

    /**
     * 转换表格引用为列编号
     *
     * @param cellReference 列引用
     * @return 表格列位置，从0开始算
     */
    public int getCellIndex(String cellReference) {
        String ref = cellReference.replaceAll("\\d+", "");
        int num = 0;
        int result = 0;
        for (int i = 0; i < ref.length(); i++) {
            char ch = cellReference.charAt(ref.length() - i - 1);
            num = (int) (ch - 'A' + 1);
            num *= Math.pow(26, i);
            result += num;
        }
        return result - 1;
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {
    }
}
