package com.kingyee.common.util.excel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2018/11/13.
 * Excel大文件读取 SAX模式
 * poi在读取excel时,可以有2种模式
 * 1.用户模式,usermodel,该模式有良好的api,缺点时,如果文件有较多的列数和行数(2000+),会有大量类生成,造成内存溢出
 * 2.事件模式,eventusermodel 该模式原理是基于Excel2007以上,微软优化了文件结构,一个excel文件其实是压缩包,
 * 里面有许多xml文件,该模式读取xml数据,优点是速度超快,占用内存小,缺点是api不太友好
 * <p>
 * 说明:适用于excel2007以上导入
 * 日期的读取,根据单元格设置的格式不同,最终可能格式不统一
 * 所以对于日期列可以统一单元格格式为 日期,类型:2012/3/14,不要选带星号*的(跟随操作系统展示日期格式)
 * 其他字符串,数字列,布尔值等均可以正常读出
 * 官方:http://poi.apache.org/components/spreadsheet/how-to.html#xssf_sax_api
 * If memory footprint is an issue, then for XSSF, you can get at the underlying XML data, and process it yourself
 * XSSFSheetXMLHandler,是官方提供的一个封装,可以不必像官方文档那样一个个读取Cell了,可再度封装来使用
 */
public class ExcelParser {
    private static final Log log = LogFactory.getLog(ExcelParser.class);
    /**
     * 表格默认处理器
     */
    private SimpleSheetHandler contentHandler = new SimpleSheetHandler();
    /**
     * 读取数据
     */
    private List<String[]> datas = new ArrayList<String[]>();

    /**
     * 转换表格,默认为转换第一个表格
     *
     * @param stream        表格输入流
     * @param columnsLength 读取多少列
     * @return 表格转换器
     */
    public ExcelParser parse(InputStream stream, int columnsLength) throws Exception {
        return parse(stream, 1, columnsLength);
    }

    /**
     * 转换表格
     *
     * @param stream        表格输入流
     * @param sheetId       为要遍历的sheet索引，从1开始
     * @param columnsLength 读取多少列
     * @return 表格转换器
     */
    public synchronized ExcelParser parse(InputStream stream, int sheetId, int columnsLength) throws Exception {
        // 每次转换前都清空数据
        datas.clear();
        // 打开表格文件输入流
        OPCPackage pkg = OPCPackage.open(stream);
        try {
            // 创建表阅读器
            XSSFReader reader;
            try {
                reader = new XSSFReader(pkg);
            } catch (OpenXML4JException e) {
                throw new Exception(e.fillInStackTrace());
            }
            // 转换指定单元表
            InputStream shellStream = reader.getSheet("rId" + sheetId);
            try {
                InputSource sheetSource = new InputSource(shellStream);
                StylesTable styles = reader.getStylesTable();
                ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
                // 设置读取出的数据
                getContentHandler().init(datas, columnsLength);
                // 获取转换器
                XMLReader parser = getSheetParser(styles, strings);
                parser.parse(sheetSource);
            } catch (SAXException e) {
                log.error("读取表格出错");
                throw new Exception(e.fillInStackTrace());
            } finally {
                shellStream.close();
            }
        } finally {
            pkg.close();

        }
        return this;

    }

    /**
     * 获取表格读取数据,获取数据前，需要先转换数据<br>
     * 此方法不会获取第一行数据
     *
     * @return 表格读取数据
     */
    public List<String[]> getDatas() {
        return getDatas(true);

    }

    /**
     * 获取表格读取数据,获取数据前，需要先转换数据
     *
     * @param dropFirstRow 删除第一行表头记录
     * @return 表格读取数据
     */
    public List<String[]> getDatas(boolean dropFirstRow) {
        // 删除表头
        if (dropFirstRow && datas.size() > 0) {
            datas.remove(0);
        }
        return datas;
    }

    /**
     * 获取读取表格的转换器
     *
     * @return 读取表格的转换器
     * @throws SAXException SAX错误
     */
    protected XMLReader getSheetParser(StylesTable styles, ReadOnlySharedStringsTable strings) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
        parser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, getContentHandler(), false));
        return parser;
    }

    public SimpleSheetHandler getContentHandler() {
        return contentHandler;
    }

    public void setContentHandler(SimpleSheetHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    /**
     * 示例
     * test
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Long time = System.currentTimeMillis();
            File file = new File("C:\\Users\\zhanghongliang\\Desktop\\重庆儿童\\AD初筛数据.xlsx");
            ExcelParser parser = new ExcelParser();
            parser.parse(new FileInputStream(file), 1, 149);
            List<String[]> list = parser.getDatas(true);
            Long endtime = System.currentTimeMillis();
            System.out.println("解析数据耗时" + (endtime - time) / 1000 + "秒");
            for (String[] strings : list) {
                System.out.println(strings[0] + ":" + strings[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
