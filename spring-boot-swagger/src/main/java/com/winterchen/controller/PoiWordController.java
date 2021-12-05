package com.winterchen.controller;

import com.winterchen.util.BokeWordUtils;
import com.winterchen.util.FolderToZipUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value = "/hello")
@Api(tags = "1.1", description = "poi管理", value = "poi管理")
public class PoiWordController {
    @PostMapping(value = "/word")
    public String helloWord(@RequestParam(value = "name") String string) {
        return string;
    }

    /**
     * 服务端文件夹打包成zip文件，然后下载
     * @param response
     *  http://localhost:8088/hello/downloadZip
     */
    @GetMapping(value = "/downloadZip")
    public void downloadZip(HttpServletResponse response) {
        String basePath = FolderToZipUtil.getRootPath();
        String s = UUID.randomUUID().toString();
        String zipPath = basePath+"filesource/download/"+s;
        try {
            //先删除指定的目录
             //FolderToZipUtil.clearFiles(zipPath);
            //模板文件地址
            String inputUrl = basePath+"filesource/test.docx";
            FolderToZipUtil.createDir(zipPath);
            //新生产的模板文件
            String outputUrl =  basePath+"filesource/download/"+s+"/test1.docx";
            Map<String, String> testMap = new HashMap<String, String>();
            testMap.put("name", "小明");
            testMap.put("sex", "男");
            testMap.put("nling", "18");
            testMap.put("address", "北京市");
            testMap.put("neirong", "好的内容");
            testMap.put("address", "软件园");
            testMap.put("xuehao", "88888888");

            List<String[]> testList = new ArrayList<String[]>();
            testList.add(new String[]{"1","1mm","1开开","1uu"});
            testList.add(new String[]{"2","2密码","2B","基金C"});
            testList.add(new String[]{"3","3看看","3B","基看C"});
            testList.add(new String[]{"4","4累了","4B","4谁说的"});
            BokeWordUtils.changWord(inputUrl, outputUrl, testMap, testList);

            //执行目录压缩下载
            FolderToZipUtil.zip(zipPath,response);

            FolderToZipUtil.clearFiles(zipPath);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            FolderToZipUtil.clearFiles(zipPath);
        }
    }
}
