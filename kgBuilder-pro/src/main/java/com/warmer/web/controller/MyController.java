package com.warmer.web.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.warmer.web.entity.MyData;
import com.warmer.web.entity.ZdcqData;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class MyController extends BaseController{
    @Autowired
    private ObjectMapper objectMapper; // 注入ObjectMapper
    @PostMapping("/data")
    public void processData(@RequestBody MyData myData) {
        try {
            // 设置Python文件路径和参数
            String pythonFile = "cq.py";
            // 在这里处理接收到的 JSON 数据
            String text = myData.getText();
            String zhuti = myData.getZhuti();
            String gx1 = myData.getGx1();
            String gx2 = myData.getGx2();
            String gx3 = myData.getGx3();
//            String zhuti = "竞赛名称";
//            String gx1 = "主办方";
//            String gx2 = "承办方";
//            String gx3 = "已举办次数";
//            String text = "2022语言与智能技术竞赛由中国中文信息学会和中国计算机学会联合主办，百度公司、中国中文信息学会评测工作委员会和中国计算机学会自然语言处理专委会承办，已连续举办4届，成为全球最热门的中文NLP赛事之一";

            // 构建命令列表
            ProcessBuilder pb = new ProcessBuilder("python", pythonFile);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectErrorStream(true);

            // 设置环境变量
            pb.environment().put("zhuti", zhuti);
            pb.environment().put("gx1", gx1);
            pb.environment().put("gx2", gx2);
            pb.environment().put("gx3", gx3);
            pb.environment().put("text", text);

            // 启动进程并执行命令
            Process process = pb.start();

            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
    @PostMapping("/zdcq")
    public void zdcq(@RequestBody ZdcqData zdcqData){
        String fact = zdcqData.getFact();
        // 发送POST请求到接口
        try {
            // 构建URL对象
            URL url = new URL("http://localhost:8385/api/v1/labelContent");

            // 打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设置请求方法
            conn.setRequestMethod("POST");

            // 设置请求头部
            conn.setRequestProperty("Content-Type", "application/json");

            // 启用输出流
            conn.setDoOutput(true);

            // 构建请求体
            String jsonBody = "{\"wsContent\": \"" + fact + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 发送请求并获取响应
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Response: " + response.toString());
                String re = response.toString();
                // Response: {"code":200,"message":"Success","data":"[{刘某某， 被告人， 开阳县南山社区三中小区民兴路7号},{曹某某， 受害者， 被盗物品包括铂金手镯、千足金项链、黄金戒指、彩金耳环、华为荣耀8手机和苹果6手机},{开阳县价格认证中心， 涉案物品价格认定机构， 涉案物品},{被害人曹某某， 被盗物品的实际所有人， 铂金手镯， 千足金项链},{铂金手镯， 价值， 人民币12838元},{千足金项链， 价值， 人民币12838元},{黄金戒指， 价值， 人民币12838元},{彩金耳环， 价值， 人民币12838元},{华为荣耀8手机， 价值， 人民币12838元},{苹果6手机， 价值， 人民币12838元}]"}
                try {
                    JSONObject jsonObject = new JSONObject(re);
                    String re2 = jsonObject.getString("data");

                    System.out.println(re2);
                    // re2 为 "[{刘某某， 被告人， 开阳县南山社区三中小区民兴路7号},{曹某某， 受害者， 被盗物品包括铂金手镯、千足金项链、黄金戒指、彩金耳环、华为荣耀8手机和苹果6手机},{开阳县价格认证中心， 涉案物品价格认定机构， 涉案物品},{被害人曹某某， 被盗物品的实际所有人， 铂金手镯， 千足金项链},{铂金手镯， 价值， 人民币12838元},{千足金项链， 价值， 人民币12838元},{黄金戒指， 价值， 人民币12838元},{彩金耳环， 价值， 人民币12838元},{华为荣耀8手机， 价值， 人民币12838元},{苹果6手机， 价值， 人民币12838元}]"
                    List<List<String>> data2 = new ArrayList<>();

                    Pattern pattern = Pattern.compile("\\{(.*?)\\}");
                    Matcher matcher = pattern.matcher(re2);

                    while (matcher.find()) {
                        String[] parts = matcher.group(1).split("，");
                        if (parts.length >= 3) {
                            // Swap the 2nd and 3rd elements
                            String temp = parts[1];
                            parts[1] = parts[2];
                            parts[2] = temp;
                        }
                        List<String> rowData = new ArrayList<>();
                        for (String part : parts) {
                            rowData.add(part.trim());
                        }
                        data2.add(rowData);
                    }

                    try (Workbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("Data");

                        int rowNum = 0;
                        for (List<String> rowData : data2) {
                            Row row = sheet.createRow(rowNum++);
                            int colNum = 0;
                            for (String value : rowData) {
                                row.createCell(colNum++).setCellValue(value);
                            }
                        }

                        FileOutputStream outputStream = new FileOutputStream("xyz.xlsx");
                        workbook.write(outputStream);
                        workbook.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            // 关闭连接
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
