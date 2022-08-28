package com.hundsun.demo.java.hs;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ProductName: Java
 * @Package: org.hl.java.util
 * @Description: 快速创建Request文件和Response文件
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */
@Slf4j
public class TranslateTool {

    /**
     * 是否需要分页标记
     */
    private static Boolean isNeedPage;
    /**
     * A
     */
    private final static Character MIN_UPPER_CHAR = 'A';
    /**
     * Z
     */
    private final static Character MAX_UPPER_CHAR = 'Z';
    /**
     * Request
     */
    private final static String STRING_REQUEST = "Request";
    /**
     * 输入参数
     */
    private final static String STRING_INPUT_PARAM = "输入参数";
    /**
     * 输出参数
     */
    private final static String STRING_OUTPUT_PARAM = "输出参数";
    /**
     * 业务说明
     */
    private final static String STRING_BUSINESS_DESCRIPTION_PARAM = "业务说明";

    /******************************************TODO 以下内容可能根据需要手动修改 ***************************************/

    /**
     * TODO 接口列表文件路径
     */
    private final static String API_LIST_FILE_PATH = "C:\\Users\\hulei42031\\Desktop\\股票期权接口文件\\接口列表.xlsx";
    /**
     * TODO 接口详情文件路径
     */
    private final static String API_DETAILS_FILE_PATH = "C:\\Users\\hulei42031\\Desktop\\股票期权接口文件\\接口详情.xlsx";
    /**
     * TODO 类型对照
     */
    private final static String API_PARAM_TYPE_INFO_PATH = "C:\\Users\\hulei42031\\Desktop\\股票期权接口文件\\类型对照.xlsx";
    /**
     * TODO 输出Request/Response文件夹路径
     */
    private final static String OUTPUT_FOLDER_PATH = ".\\java-demo\\src\\main\\resources\\fileResource\\";
    /**
     * TODO 接口列表文件中功能号所在格子的下标
     */
    private final static Integer FUNCTION_ID_INDEX_API_LIST = 4;
    /**
     *
     */
    private final static Integer FUNCTION_MENU_INDEX_API_LIST = 1;
    /**
     * TODO 接口详情文件中菜单所在格子的下标
     */
    private final static Integer MENU_INDEX_API_DETAILS = 1;
    /**
     * TODO 接口详情文件中功能号所在格子的下标
     */
    private final static Integer FUNCTION_ID_INDEX_API_DETAILS = 2;
    /**
     * TODO 接口详情文件中功能号中文名称所在格子的下标
     */
    private final static Integer FUNCTION_CH_NAME_INDEX_API_DETAILS = 2;
    /**
     * TODO 接口详情文件中功能号英文名称所在格子的下标
     */
    private final static Integer FUNCTION_EN_NAME_INDEX_API_DETAILS = 2;
    /**
     * TODO 接口详情文件中功能的输入参数所在格子的下标
     */
    private final static Integer FUNCTION_INPUT_PARAM_INDEX_API_DETAILS = 2;
    /**
     * TODO 接口详情文件中功能的输出参数所在格子的下标
     */
    private final static Integer FUNCTION_OUTPUT_PARAM_INDEX_API_DETAILS = 2;
    /**
     * TODO 接口详情文件中功能的输入参数的类型所在格子的下标
     */
    private final static Integer FUNCTION_INPUT_PARAM_TYPE_INDEX_API_DETAILS = 3;
    /**
     * TODO 接口详情文件中功能的输出参数的类型所在格子的下标
     */
    private final static Integer FUNCTION_OUTPUT_PARAM_TYPE_INDEX_API_DETAILS = 3;
    /**
     * TODO 接口详情文件中功能的输入参数的说明所在格子的下标
     */
    private final static Integer FUNCTION_INPUT_PARAM_INFO_INDEX_API_DETAILS = 4;
    /**
     * TODO 接口详情文件中功能的输出参数的说明所在格子的下标
     */
    private final static Integer FUNCTION_OUTPUT_PARAM_INFO_INDEX_API_DETAILS = 4;

    public static void main(String[] args) throws Exception {

        excel();


    }

    /**
     * 参数名进行转换
     *
     * @param paramName
     */
    public static String getRealParam(String paramName) {

        String realParam = "";

        //判断参数名是否存在ID
        int idIndex = isExistID(paramName);
        // 参数首字母小写，以及ID驼峰
        realParam += paramName.substring(0, 1).toLowerCase();
        for (int i = 1; i < paramName.length(); i++) {
            realParam += paramName.substring(i, i + 1);
            if (i == idIndex) {
                ++i;
                realParam += paramName.substring(i, i + 1).toLowerCase();
            }
        }

        return realParam;

    }

    /**
     * 判断字符串是否存在ID,并返回ID所在下标
     *
     * @return
     */
    public static Integer isExistID(String param) {

        for (int i = 0; i < param.length(); i++) {

            if (param.charAt(i) == 'I' && i + 1 < param.length() && param.charAt(i + 1) == 'D') {
                return i;
            }
        }
        return -1;
    }

    /**
     * 生成对应的Request/Response文件
     *
     * @param reBody
     */
    public static String createFile(ReBody reBody, String outPath) {
        String serviceCodeReBodyType = "";
        if (reBody.getFunctionIdChName() == null || reBody.getFunctionIdParam().isEmpty()) {
            // 没有参数(入参或出参)
        } else {
            // 生成的文件名
            String fileName = getClassName(reBody.getFunctionIdEnName()) + reBody.getClass().getSimpleName() + "DTO.java";
            // 生成的文件
            File file = new File(outPath + fileName);
            // 获取类名
            String className = fileName.substring(0, fileName.length() - 5);
            serviceCodeReBodyType += className;
            // TODO 包名(根据需要修改)
            String packageString = "package com.hundsun.amust.stockopt.api.model." + reBody.getClass().getSimpleName().substring(0, 1).toLowerCase() + reBody.getClass().getSimpleName().substring(1) + ";" +
                    "\n" +
                    "\n";
            // 引入包
            String importString = "import com.hundsun.amust.common.annotation.UstSingleParam;\n" +
                    "import lombok.Data;\n" +
                    "import java.io.Serializable;\n";
            // 如果是Request需要继承BasePageRequestDTO类，所以要引入这个包
            if (STRING_REQUEST.equals(reBody.getClass().getSimpleName())) {
                importString += "import com.hundsun.amust.common.model.request.BasePageRequestDTO;\n";
            }

            importString += "\n";
            // @UstField注解，后面判断是否引入
            String ustFieldPackageString = "";
            // 注释模板
            String javadocString = "/**\n" +
                    " * @ProductName: Hundsun HEP\n" +
                    " * @ProjectName: stockopt\n" +
                    " * @Package: com.hundsun.amust.stockopt.api.model." + reBody.getClass().getSimpleName().substring(0, 1).toLowerCase() + reBody.getClass().getSimpleName().substring(1) + "\n" +
                    " * @Description:" + reBody.getFunctionIdChName() + "\n" +
                    " * @Author: hulei42031\n" +
                    " * @Date: 2022/02/18 14:20\n" +
                    " * @UpdateRemark:\n" +
                    " * @Version: 1.0\n" +
                    " * <p>\n" +
                    " * Copyright © 2022 Hundsun Technologies Inc. All Rights Reserved\n" +
                    " */" +
                    "\n" +
                    "\n";
            // 类注解
            String annotationString = "@UstSingleParam\n" +
                    "@Data" +
                    "\n";
            // 类头
            String headString = "public class " + className;
            // 如果是Request需要继承BasePageRequestDTO类
            if (STRING_REQUEST.equals(reBody.getClass().getSimpleName())) {
                headString += " extends BasePageRequestDTO";
            }

            headString += " implements Serializable {" +
                    "\n" +
                    "\n";
            // 参数详情（参数注释+参数声明）
            String bodyString = "";
            Map<String, String> paramTypes = reBody.getFunctionIdType();
            Map<String, String> paramInfos = reBody.getFunctionIdInfo();
            ArrayDeque<String> param = (ArrayDeque) reBody.getFunctionIdParam();
            while (!param.isEmpty()) {
                String paramName = param.removeFirst();
                // 参数名如果是TotalAction则不追加
                if ("TotalAction".equals(paramName) || paramName == "") {
                    isNeedPage = true;
                    continue;
                } else {

                    // 追加参数注释
                    bodyString += "    /**" +
                            "\n";
                    // 如果属性有说明则追加说明，否则直接追加属性名
                    if (paramInfos.containsKey(paramName)) {
                        bodyString += "     * " + paramInfos.get(paramName) +
                                "\n";
                    } else {
                        bodyString += paramName +
                                "\n";
                    }
                    bodyString += "     */" +
                            "\n";
                    // 追加参数声明,判断参数是否含有ID
                    if (isExistID(paramName) != -1) {
                        // 含有ID那么需要加上@UstField注解
                        ustFieldPackageString = "import com.hundsun.amust.common.annotation.UstField;" +
                                "\n";
                        bodyString += "    @UstField(value = \"" + paramName + "\")" +
                                "\n";
                        bodyString += "    private " + paramTypes.get(paramName) + " " + getRealParam(paramName) + ";" +
                                "\n";
                    } else {
                        bodyString += "    private " + paramTypes.get(paramName) + " " + getRealParam(paramName) + ";" +
                                "\n";
                    }
                }


            }
            bodyString += "}";

            // System.out.println(packageString+ustFieldPackageString+importString+javadocString+annotationString+headString+bodyString);

            try {
                FileWriter out = new FileWriter(file);
                out.write(packageString + ustFieldPackageString + importString + javadocString + annotationString + headString + bodyString);
                out.flush();
                out.close();
                log.info("成功生成文件：" + fileName);
            } catch (Exception e) {
                log.error("the method createServiceCode catch the error : " + e.toString());
            }

        }

        return serviceCodeReBodyType;

    }

    /**
     * 获取生成的类名
     *
     * @param className
     * @return
     */
    public static String getClassName(String className) {
        String clazzName = className.replaceAll("Req", "");
        int old = clazzName.length();
        String clazzName1 = clazzName.replaceAll("Query", "");
        if (clazzName1.length() < old) {
            return clazzName1 + "Query";
        }
        clazzName1 = clazzName.replaceAll("Mode", "");
        if (clazzName1.length() < old) {
            return clazzName1 + "Mode";
        }
        clazzName1 = clazzName.replaceAll("Mod", "");
        if (clazzName1.length() < old) {
            return clazzName1 + "Mod";
        }
        clazzName1 = clazzName.replaceAll("Add", "");
        if (clazzName1.length() < old) {
            return clazzName1 + "Add";
        }
        clazzName1 = clazzName.replaceAll("Remove", "");
        if (clazzName1.length() < old) {
            return clazzName1 + "Remove";
        }
        clazzName1 = clazzName.replaceAll("Del", "");
        if (clazzName1.length() < old) {
            return clazzName1 + "Del";
        }
        clazzName1 = clazzName.replaceAll("Start", "");
        if (clazzName1.length() < old) {
            return clazzName1 + "Start";
        }
        clazzName1 = clazzName.replaceAll("Stop", "");
        if (clazzName1.length() < old) {
            return clazzName1 + "Stop";
        }
        return clazzName;
    }

    /**
     * 读取excel表，生成文件
     */
    public static void excel() throws Exception {

        // 接口列表文件
        File apiListFile = new File(API_LIST_FILE_PATH);
        // 接口详情文件
        File apiDetailsFile = new File(API_DETAILS_FILE_PATH);


        // 接口列表文件
        XSSFWorkbook apiListWorkbook = new XSSFWorkbook(new FileInputStream(apiListFile));
        // 获取的所有功能号
        List<FunctionInfo> functionIds = getFunctionIds(apiListWorkbook);
        apiListWorkbook.close();


        // 接口详情文件
        XSSFWorkbook apiDetailsWorkbook = new XSSFWorkbook(new FileInputStream(apiDetailsFile));
        // 声明一个list用于接受每个功能号对应的输入参数，输出参数
        List<List> param = getFunctionIdsParam(functionIds, apiDetailsWorkbook);
        apiDetailsWorkbook.close();

        // 输入参数
        List<Request> requests = param.get(0);
        // 输出参数
        List<Response> responses = param.get(1);


        String createServiceCodeRequestName = "";
        String createServiceCodeResponseName = "";

        for (int i = 0; i < requests.size(); i++) {
            isNeedPage = new Boolean(false);
            log.info("接口名：" + requests.get(i).getFunctionIdChName() + "-" + requests.get(i).getFunctionId() + "-" + requests.get(i).getFunctionIdEnName());
            String functionEnName = requests.get(i).getFunctionIdEnName().replaceAll("Req", "");
            String ustFunctionConsts = "public static final String FUNC_UST";
            for (int j = 0; j < functionEnName.length(); j++) {
                if (functionEnName.charAt(j) >= 'A' && functionEnName.charAt(j) <= 'Z') {
                    ustFunctionConsts += "_" + functionEnName.charAt(j);
                } else {
                    ustFunctionConsts += ("" + functionEnName.charAt(j)).toUpperCase();
                }
            }
            ustFunctionConsts += " = \"" + requests.get(i).getFunctionId().substring(0, requests.get(i).getFunctionId().length() - 2) + "\";";
            log.info("UstFunctionConsts：" + ustFunctionConsts);

            File file = new File(OUTPUT_FOLDER_PATH + requests.get(i).getMenuName());
            if (file.exists()) {
            } else {
                new File(OUTPUT_FOLDER_PATH + requests.get(i).getMenuName()).mkdirs();
            }

            if (new File(OUTPUT_FOLDER_PATH + requests.get(i).getMenuName() + "\\" + requests.get(i).getFunctionIdChName() + "-" + requests.get(i).getFunctionId().substring(0, requests.get(i).getFunctionId().length() - 2)).mkdirs()) {
                createServiceCodeRequestName = createFile(requests.get(i), OUTPUT_FOLDER_PATH + requests.get(i).getMenuName() + "\\" + requests.get(i).getFunctionIdChName() + "-" + requests.get(i).getFunctionId().substring(0, requests.get(i).getFunctionId().length() - 2) + "\\");
                createServiceCodeResponseName = createFile(responses.get(i), OUTPUT_FOLDER_PATH + requests.get(i).getMenuName() + "\\" + requests.get(i).getFunctionIdChName() + "-" + requests.get(i).getFunctionId().substring(0, requests.get(i).getFunctionId().length() - 2) + "\\");
            }

            if (new File(OUTPUT_FOLDER_PATH + requests.get(i).getMenuName()).mkdirs()) {

            }

            // createServiceCode(createServiceCodeRequestName, createServiceCodeResponseName, requests.get(i).getFunctionIdChName(), requests.get(i).getFunctionIdEnName(), outputPath);
        }


    }

    /**
     * 对参数进行转换
     *
     * @param type
     * @return
     */
    public static String paramTypeExchange(String type) {

        File file = new File(API_PARAM_TYPE_INFO_PATH);

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = workbook.getSheetAt(0);

            int index = 0;

            while (null != sheet.getRow(index)) {

                XSSFRow row = sheet.getRow(index);

                if (null != row.getCell(1) && type.equals(row.getCell(1).toString())) {
                    return row.getCell(3).toString();
                }

                if (null != row.getCell(2) && type.equals(row.getCell(2).toString())) {
                    return row.getCell(3).toString();
                }

                if (++index > sheet.getLastRowNum()) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

        return "";

    }

    /**
     * 获取功能号
     *
     * @param apiListWorkbook
     * @return
     */
    public static List<FunctionInfo> getFunctionIds(XSSFWorkbook apiListWorkbook) {

        // 获取功能号的表
        XSSFSheet apiListSheet = apiListWorkbook.getSheetAt(0);

        // 存储功能号
        List<FunctionInfo> allFunctions = new ArrayList<>();

        for (int i = 1; i <= apiListSheet.getLastRowNum(); i++) {

            XSSFRow row = apiListSheet.getRow(i);

            FunctionInfo functionInfo = new FunctionInfo();
            if (null != row.getCell(FUNCTION_MENU_INDEX_API_LIST) && null != row.getCell(FUNCTION_ID_INDEX_API_LIST) && !row.getCell(FUNCTION_ID_INDEX_API_LIST).getCellStyle().getFont().getStrikeout()
                    && !"".equals(row.getCell(FUNCTION_ID_INDEX_API_LIST).toString())) {
                functionInfo.setMenuName(row.getCell(FUNCTION_MENU_INDEX_API_LIST).toString());
                functionInfo.setFunctionId(row.getCell(FUNCTION_ID_INDEX_API_LIST).toString());
                functionInfo.setAlreadyWrite(false);
                allFunctions.add(functionInfo);
            }


        }
        log.info("成功获取到的功能号有：" + allFunctions.toString());
        log.info("数量为：" + allFunctions.size());
        return allFunctions;
    }

    /**
     * 获取功能号所有的数据(参数名、参数类型、参数说明)
     *
     * @param functionIds
     * @param apiDetailsWorkbook
     * @return
     */
    public static List<List> getFunctionIdsParam(List<FunctionInfo> functionIds, XSSFWorkbook apiDetailsWorkbook) {

        List<Request> requests = new ArrayList<>();
        List<Response> responses = new ArrayList<>();

        // 获取功能号数据的表
        XSSFSheet apiDetailsSheet = apiDetailsWorkbook.getSheetAt(0);

        for (int i = 0; i < apiDetailsSheet.getLastRowNum(); i++) {

            // 寻找匹配的功能号
            XSSFRow row = apiDetailsSheet.getRow(i);

            // 如果当前行为null则跳过该行
            if (row != null) {
                // 获取功能号所在单元格数据
                XSSFCell functionIdCell = row.getCell(FUNCTION_ID_INDEX_API_DETAILS);
                // 找到对应功能号（格子不为空+功能号匹配成功+无删除线）
                if (functionIdCell != null && !functionIdCell.getCellStyle().getFont().getStrikeout()) {

                    for (FunctionInfo functionInfo : functionIds) {
                        if (functionInfo.getFunctionId().equals(functionIdCell.toString())) {
                            // 当前功能号
                            String currentFunctionId = functionIdCell.toString();
                            // log.info("当前进行取参操作的功能号为：" + currentFunctionId);
                            Request request = new Request(currentFunctionId);
                            Response response = new Response(currentFunctionId);

                            request.setMenuName(functionInfo.getMenuName());
                            response.setMenuName(functionInfo.getMenuName());

                            // TODO 获取功能号的功能中文名称(下标可能会根据需要修改)
                            row = apiDetailsSheet.getRow(++i);
                            String chName = row.getCell(FUNCTION_CH_NAME_INDEX_API_DETAILS).toString();
                            request.setFunctionIdChName(chName);
                            response.setFunctionIdChName(chName);

                            // TODO 获取功能号的功能英文名称(下标可能会根据需要修改)
                            row = apiDetailsSheet.getRow(++i);
                            String enName = row.getCell(FUNCTION_EN_NAME_INDEX_API_DETAILS).toString();
                            request.setFunctionIdEnName(enName);
                            response.setFunctionIdEnName(enName);

                            // 寻找输入参数,如果没有匹配上,就往下查
                            while (row == null || row.getCell(MENU_INDEX_API_DETAILS) == null
                                    || !STRING_INPUT_PARAM.equals(row.getCell(MENU_INDEX_API_DETAILS).toString())) {

                                ++i;
                                row = apiDetailsSheet.getRow(i);

                                // 找到输入参数这个格子（开始录入数据）
                                if (row != null && row.getCell(MENU_INDEX_API_DETAILS) != null
                                        && STRING_INPUT_PARAM.equals(row.getCell(MENU_INDEX_API_DETAILS).toString())) {

                                    ++i;
                                    row = apiDetailsSheet.getRow(i);

                                    // 菜单格子是空的或者不是输出参数则未结束，录入数据
                                    while (row.getCell(MENU_INDEX_API_DETAILS) == null
                                            || !STRING_OUTPUT_PARAM.equals(row.getCell(MENU_INDEX_API_DETAILS).toString())) {

                                        // 参数格子非空+参数格子没有删除线+参数格子必须要有内容
                                        if (row.getCell(FUNCTION_INPUT_PARAM_INDEX_API_DETAILS) != null
                                                && !row.getCell(FUNCTION_INPUT_PARAM_INDEX_API_DETAILS).getCellStyle().getFont().getStrikeout()
                                                && !"".equals(row.getCell(FUNCTION_INPUT_PARAM_INDEX_API_DETAILS).toString())
                                                && !" ".equals(row.getCell(FUNCTION_INPUT_PARAM_INDEX_API_DETAILS).toString())) {

                                            // 录入参数名
                                            request.getFunctionIdParam()
                                                    .add(row.getCell(FUNCTION_INPUT_PARAM_INDEX_API_DETAILS).toString());
                                            // 录入参数类型
                                            request.getFunctionIdType()
                                                    .put(row.getCell(FUNCTION_INPUT_PARAM_INDEX_API_DETAILS).toString(),
                                                            paramTypeExchange((row.getCell(FUNCTION_INPUT_PARAM_TYPE_INDEX_API_DETAILS) == null) ? "" : row.getCell(FUNCTION_INPUT_PARAM_TYPE_INDEX_API_DETAILS).toString()));
                                            // 录入参数说明
                                            request.getFunctionIdInfo()
                                                    .put(row.getCell(FUNCTION_INPUT_PARAM_INDEX_API_DETAILS).toString(),
                                                            (row.getCell(FUNCTION_INPUT_PARAM_INFO_INDEX_API_DETAILS) == null) ? "" : row.getCell(FUNCTION_INPUT_PARAM_INFO_INDEX_API_DETAILS).toString());
                                        }
                                        ++i;
                                        row = apiDetailsSheet.getRow(i);
                                    }
                                    break;
                                }
                            }

                            //此处结束时停留在输出参数菜单格子，继续往下走
                            ++i;
                            row = apiDetailsSheet.getRow(i);

                            // 紧接着录入输出参数（菜单格子内容为业务说明时停止）
                            while (row.getCell(MENU_INDEX_API_DETAILS) == null
                                    || !STRING_BUSINESS_DESCRIPTION_PARAM.equals(row.getCell(MENU_INDEX_API_DETAILS).toString())) {

                                // 参数格子非空+参数格子没有删除线
                                if (row.getCell(FUNCTION_OUTPUT_PARAM_INDEX_API_DETAILS) != null
                                        && !row.getCell(FUNCTION_OUTPUT_PARAM_INDEX_API_DETAILS).getCellStyle().getFont().getStrikeout()
                                        && !"".equals(row.getCell(FUNCTION_OUTPUT_PARAM_INDEX_API_DETAILS).toString())
                                        && !" ".equals(row.getCell(FUNCTION_OUTPUT_PARAM_INDEX_API_DETAILS).toString())) {

                                    // 录入参数名
                                    response.getFunctionIdParam().add(row.getCell(FUNCTION_OUTPUT_PARAM_INDEX_API_DETAILS).toString());
                                    // 录入参数类型
                                    response.getFunctionIdType().put(row.getCell(FUNCTION_OUTPUT_PARAM_INDEX_API_DETAILS).toString(),
                                            paramTypeExchange((row.getCell(FUNCTION_OUTPUT_PARAM_TYPE_INDEX_API_DETAILS) == null) ? "" : row.getCell(FUNCTION_OUTPUT_PARAM_TYPE_INDEX_API_DETAILS).toString()));
                                    // 录入参数说明
                                    response.getFunctionIdInfo().put(row.getCell(FUNCTION_OUTPUT_PARAM_INDEX_API_DETAILS).toString(),
                                            (row.getCell(FUNCTION_OUTPUT_PARAM_INFO_INDEX_API_DETAILS) == null) ? "" : row.getCell(FUNCTION_OUTPUT_PARAM_INFO_INDEX_API_DETAILS).toString());
                                }
                                ++i;
                                row = apiDetailsSheet.getRow(i);
                            }
                            requests.add(request);
                            responses.add(response);
                            break;
                        }
                    }
                }
            }
        }

        List<List> result = new ArrayList<>();
        result.add(requests);
        result.add(responses);

        return result;

    }

    /**
     * 生成业务层代码
     *
     * @param requestDTOName
     * @param responseDTOName
     */
    public static void createServiceCode(String requestDTOName, String responseDTOName, String functionIdCnName,
                                         String functionIdEnName, String outputPath) {

        try {

            String fileName = outputPath + functionIdEnName + "serviceCode.txt";
            FileWriter writer = new FileWriter(fileName);
            // 判断是否需要分页
            String serviceString = "";
            String serviceImplString = "";
            String managerString = "";
            String managerImplString = "";

            if (isNeedPage) {
                serviceString += "    RpcResultDTO<PageInfo<";
                serviceImplString += "    @Override\n" +
                        "    public RpcResultDTO<PageInfo<";
                managerString += "    PageInfo<";
                managerImplString += "    @Override\n" +
                        "    public PageInfo<";
            } else {
                serviceString += "    RpcResultDTO<List<";
                serviceImplString += "    @Override\n" +
                        "    public RpcResultDTO<List<";
                managerString += "    List<";
                managerImplString += "    @Override\n" +
                        "    public List<";
            }

            serviceString += responseDTOName + ">> " + getFunctionName(functionIdEnName) + "(@Valid "
                    + requestDTOName + " " + requestDTOName.substring(0, 1).toLowerCase()
                    + requestDTOName.substring(1) + ");\n";
            serviceImplString += responseDTOName + ">> " + getFunctionName(functionIdEnName) + "("
                    + requestDTOName + " " + requestDTOName.substring(0, 1).toLowerCase()
                    + requestDTOName.substring(1) + ") {\n";
            managerString += responseDTOName + "> " + getFunctionName(functionIdEnName) + "("
                    + requestDTOName + " " + requestDTOName.substring(0, 1).toLowerCase()
                    + requestDTOName.substring(1) + ");\n";
            managerImplString += responseDTOName + "> " + getFunctionName(functionIdEnName) + "("
                    + requestDTOName + " " + requestDTOName.substring(0, 1).toLowerCase()
                    + requestDTOName.substring(1) + ") {\n";

            if (isNeedPage) {
                serviceImplString += "        PageInfo<";
                managerImplString += "        return ustCallUtil.pageCallUst(";
            } else {
                serviceImplString += "        List<";
                managerImplString += "        return ustCallUtil.listCallUst(";
            }

            serviceImplString += responseDTOName + "> " + responseDTOName.substring(0, 1).toLowerCase()
                    + responseDTOName.substring(1) + "S = xxxService." + getFunctionName(functionIdEnName) + "("
                    + requestDTOName.substring(0, 1).toLowerCase()
                    + requestDTOName.substring(1) + ");\n" +
                    "        return RpcResultProduce.getSuccessResult(" + responseDTOName.substring(0, 1).toLowerCase()
                    + responseDTOName.substring(1) + "S);\n" +
                    "    }\n";

            if (isNeedPage) {
                managerImplString += "coreService::" + getFunctionName(functionIdEnName) + ","
                        + requestDTOName.substring(0, 1).toLowerCase() + requestDTOName.substring(1)
                        + ",new " + responseDTOName + "());\n" +
                        "    }\n";
            } else {
                managerImplString += "coreService::" + getFunctionName(functionIdEnName) + ", "
                        + requestDTOName.substring(0, 1).toLowerCase() + requestDTOName.substring(1)
                        + ", " + responseDTOName + ".class);\n" +
                        "    }\n";
            }

            writer.write(serviceString + serviceImplString + managerString + managerImplString);
            writer.flush();
            writer.close();
            log.info("成功生成文件：" + functionIdCnName + "-" + functionIdEnName + "serviceCode.txt");

        } catch (Exception e) {
            log.error("the method createServiceCode catch the error : " + e.toString());
        }

    }

    /**
     * 生成方法名
     *
     * @param functionIdEnName
     * @return
     */
    public static String getFunctionName(String functionIdEnName) {
        return functionIdEnName.substring(3, 4).toLowerCase() + functionIdEnName.substring(4);
    }

}
