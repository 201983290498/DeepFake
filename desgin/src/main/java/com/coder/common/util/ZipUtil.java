package com.coder.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @Author coder
 * @Date 2022/11/2 21:53
 * @Description 压缩文件上传，解压
 */
public class ZipUtil {

    /**
     * 读写的缓冲池的大小
     */
    private static final int BUFFER_SIZE = 1024 * 2;

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);

    /**
     * detector文件夹
     */
    private static final String DETECT_DIR = "detectFile/";

    /**
     * 将zip文件的base64转换成相应的文件
     *
     * @param base64   文件的base64字符串
     * @param filename 解压的文件名
     * @param request  http的请求，获取项目在本地的地址
     * @return 返回压缩文件的地址
     */
    public static String base64ToFile(String base64, String filename, HttpServletRequest request) throws IOException {
        String contentPath = request.getSession().getServletContext().getRealPath("/");
        File dir = new File(contentPath + DETECT_DIR + UUID.randomUUID().toString().substring(0, 6));
        // 查看检测文件夹/context/detectFile是否存在
        if (!dir.getParentFile().exists()) {
            dir.getParentFile().mkdir();
        }
        // 创建检测文件夹/context/detectFile/path
        if (!dir.exists()) {
            dir.mkdir();
        }

        // 文件存放的地址
        String filePath = dir.getAbsolutePath().concat("\\").concat(filename);
        if (!dir.exists()) {
            dir.mkdir();
        }

        // 先将文件写出来在解压, 将base64去掉文件头
        base64 = base64.substring(base64.indexOf(',') + 1);
        byte[] bytes = Base64.getDecoder().decode(base64);
        OutputStream out;
        out = Files.newOutputStream(Paths.get(filePath));
        out.write(bytes);
        out.flush();
        out.close();
        return filePath;
    }

    /**
     * 获取压缩文件的字节, 和解压路径, 将文件解压。
     * @param bytes 压缩文件字节
     * @param unZipPath 解压路径
     */
    public static void unZipFile(byte[] bytes, String unZipPath){
        File dir = new File(unZipPath);
        ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
        ZipInputStream zipInput = new ZipInputStream(byteArray);
        ZipEntry entry;
        try {
            entry = zipInput.getNextEntry();
            File fout;
            while(entry != null ){
                if (!entry.isDirectory()){
                    log.info("文件名称： [{}]", entry.getName());
                    String fileName = entry.getName();
                    if (fileName.lastIndexOf("/") != -1) {
                        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                    }
                    fout = new File(dir, fileName);
                    BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(fout.toPath()));
                    int offset;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((offset = zipInput.read(buffer)) != -1) {
                        bos.write(buffer, 0, offset);
                    }
                    bos.close();
                    // 获取下一个文件
                }
                entry = zipInput.getNextEntry();
            }
            zipInput.close();
            byteArray.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * todo 待检测 待拆分, 需要将函数拆分成生成zip文件和读取文件变成base64
     *
     * @param arcFiles 需要压缩的文件列表
     * @return 返回base64字符串
     */
    public static String zipToBase64(List<File> arcFiles) throws RuntimeException {
        log.info("开始压缩文件 [{}]", arcFiles);
        // 获取压缩文件的时间
        long start = System.currentTimeMillis();
        // base64字符串
        String base64toZip;
        // zip输出流
        ZipOutputStream zos = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 将字节流转换成Zip输出流
            zos = new ZipOutputStream(baos);
            for (File srcFile : arcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                log.info("压缩的文件名称 [{}]", srcFile.getName() + "压缩的文件大小    [{}]", srcFile.length());
                /* 创建一个zip项目 */
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                // 读取文件，并写入到zip文件夹中
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry(); // 关闭zip项目
                in.close();
                long end = System.currentTimeMillis();
                log.info("压缩完成，耗时: [{}] ms", (end - start));
            }
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipToBase64", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] referFileBase64Bytes = Base64.getEncoder().encode(baos.toByteArray());
        base64toZip = new String(referFileBase64Bytes, StandardCharsets.UTF_8);
        return "data:application/zip;base64," + base64toZip;
    }

    /**
     * 压缩成zip文件
     *
     * @param srcDir           压缩文件夹的路径
     * @param out              压缩文件输出流
     * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void folderToZip(String srcDir, OutputStream out, boolean keepDirStructure) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 递归的方式压缩文件夹
     *
     * @param sourceFile       源文件
     * @param zos              输出流
     * @param name             压缩后的文件名称
     * @param keepDirStructure 是否保留原来的目录结构,
     * @throws Exception 抛出异常
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure)
            throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // 把文件放到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 保留原来的文件结构,并对空文件夹进行处理
                if (keepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), keepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }
                }
            }
        }
    }

    /**
     * 解压大文件压缩包
     * @param filePath 大文件压缩路径
     * @param unZipPath 解压路径
     * @return 返回解压路径
     */
    public static String unZip(String filePath, String unZipPath) {
        String zipDir;
        if (unZipPath == null) {
            zipDir = filePath.substring(0, filePath.lastIndexOf("."));
        }else{
            zipDir = unZipPath;
        }
        File tem = new File(zipDir);
        if(!tem.exists()) {
            tem.mkdirs();
        }

        String name;
        try {
            BufferedOutputStream dest;
            BufferedInputStream is;
            ZipEntry entry;
            ZipFile zipfile = new ZipFile(filePath);

            Enumeration<? extends ZipEntry> dir = zipfile.entries();
            while (dir.hasMoreElements()){
                entry = dir.nextElement();
                if(entry.isDirectory()){
                    name = entry.getName();
                    name = name.substring(0, name.length() - 1);
                    File fileObject = new File(zipDir + name);
                    fileObject.mkdir();
                }
            }
            Enumeration<? extends ZipEntry> e = zipfile.entries();
            while (e.hasMoreElements()) {
                entry = e.nextElement();
                if(!entry.isDirectory()){
                    is = new BufferedInputStream(zipfile.getInputStream(entry));
                    int count;
                    byte[] dataByte = new byte[BUFFER_SIZE];
                    FileOutputStream fos = new FileOutputStream(zipDir+ "/" +entry.getName().substring(entry.getName().lastIndexOf("/")+1));
                    dest = new BufferedOutputStream(fos, BUFFER_SIZE);
                    while ((count = is.read(dataByte, 0, BUFFER_SIZE)) != -1) {
                        dest.write(dataByte, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* 删除压缩包 */
        new File(filePath).delete();
        return zipDir;
    }
}
