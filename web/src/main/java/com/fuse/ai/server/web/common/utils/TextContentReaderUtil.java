package com.fuse.ai.server.web.common.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * 文本内容读取工具类
 * 支持多种文档类型和编程源文件的文本内容提取
 */
@Component
public class TextContentReaderUtil {

    private static final Set<String> TEXT_FILE_EXTENSIONS = Set.of(
            // 纯文本文件
            "txt", "rtf", "md", "markdown",
            // 文档文件
            "doc", "docx", "pdf", "odt", "epub", "mobi", "tex", "latex",
            // 编程语言文件
            "java", "py", "c", "cpp", "cc", "cxx", "h", "hpp", "hxx", "cs", "vb",
            "html", "htm", "xhtml", "css", "js", "jsx", "ts", "tsx", "vue",
            "php", "asp", "aspx", "jsp", "sh", "bash", "zsh", "ps1", "bat", "cmd",
            "vbs", "rb", "pl", "pm", "tcl", "lua", "swift", "kt", "dart", "m", "mm",
            "hs", "scala", "clj", "r", "rs", "go", "asm", "s", "pas", "pp", "f", "for",
            "f90", "erl", "ex", "exs", "make", "mk", "cmake", "gradle", "pom",
            // 配置和数据文件
            "xml", "json", "yaml", "yml", "properties", "ini", "cfg", "conf", "toml",
            "csv", "tsv", "sql", "db", "dbf",
            // 模板文件
            "tpl", "template", "mustache", "handlebars", "ejs",
            // 其他文本格式
            "log", "out", "err", "rst", "asciidoc", "adoc", "diff", "patch",
            "msgpack", "bson", "avro", "protobuf", "proto"
    );

    private static final Set<String> BINARY_DOCUMENT_EXTENSIONS = Set.of(
            "doc", "docx", "pdf", "xls", "xlsx", "ppt", "pptx"
    );

    /**
     * 读取文件内容
     */
    public static TextReadResultUtil readContent(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(fileName).toLowerCase();

            if (!isSupportedTextFile(fileExtension)) {
                return TextReadResultUtil.unsupportedFormat(fileName, fileExtension);
            }

            String content = extractContent(file, fileExtension);
            return TextReadResultUtil.success(fileName, fileExtension, content);

        } catch (Exception e) {
            return TextReadResultUtil.error(file.getOriginalFilename(), e.getMessage());
        }
    }

    /**
     * 读取本地文件内容
     */
    public static TextReadResultUtil readContent(File file) {
        try {
            String fileName = file.getName();
            String fileExtension = getFileExtension(fileName).toLowerCase();

            if (!isSupportedTextFile(fileExtension)) {
                return TextReadResultUtil.unsupportedFormat(fileName, fileExtension);
            }

            String content = extractContent(file, fileExtension);
            return TextReadResultUtil.success(fileName, fileExtension, content);

        } catch (Exception e) {
            return TextReadResultUtil.error(file.getName(), e.getMessage());
        }
    }

    /**
     * 读取文件内容为字符串
     */
    public static TextReadResultUtil readContent(Path filePath) {
        return readContent(filePath.toFile());
    }

    /**
     * 根据文件类型提取内容
     */
    private static String extractContent(Object file, String fileExtension) throws Exception {
        // 二进制文档格式
        if (BINARY_DOCUMENT_EXTENSIONS.contains(fileExtension)) {
            return extractBinaryDocumentContent(file, fileExtension);
        }

        // 文本格式文件
        return extractTextFileContent(file, fileExtension);
    }

    /**
     * 根据URL读取远程文件内容
     * @param url 文件的URL地址
     * @return TextReadResultUtil 包含读取结果的对象
     */
    public static TextReadResultUtil readContentFromUrl(String url) {
        try {
            // 使用URL获取内容
            java.net.URL fileUrl = new java.net.URL(url);
            String fileName = extractFileNameFromUrl(url);
            String fileExtension = getFileExtension(fileName).toLowerCase();

            // 检查是否为支持的文本文件类型
            if (!isSupportedTextFile(fileExtension)) {
                return TextReadResultUtil.unsupportedFormat(fileName, fileExtension);
            }

            // 读取URL内容
            String content = readUrlContent(fileUrl);

            return TextReadResultUtil.success(fileName, fileExtension, content);
        } catch (Exception e) {
            return TextReadResultUtil.error("远程文件", e.getMessage());
        }
    }

    /**
     * 从URL中提取文件名
     * @param url URL地址
     * @return 文件名
     */
    private static String extractFileNameFromUrl(String url) {
        try {
            java.net.URL fileUrl = new java.net.URL(url);
            String path = fileUrl.getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            if (fileName.isEmpty()) {
                fileName = "remote_file_" + System.currentTimeMillis();
            }
            return fileName;
        } catch (Exception e) {
            return "remote_file_" + System.currentTimeMillis();
        }
    }

    /**
     * 读取URL内容为字符串
     * @param url URL对象
     * @return 文件内容字符串
     * @throws IOException 读取异常
     */
    private static String readUrlContent(java.net.URL url) throws IOException {
        try (InputStream inputStream = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            // 移除最后多余的换行符
            if (content.length() > 0 && content.charAt(content.length() - 1) == '\n') {
                content.deleteCharAt(content.length() - 1);
            }

            return content.toString();
        }
    }

    /**
     * 提取二进制文档内容
     */
    private static String extractBinaryDocumentContent(Object file, String fileExtension) throws Exception {
        return switch (fileExtension) {
            case "pdf" -> extractPdfContent(file);
            case "docx" -> extractDocxContent(file);
            case "doc" -> extractDocContent(file);
            case "xlsx", "xls" -> extractExcelContent(file);
            case "pptx", "ppt" -> extractPowerPointContent(file);
            default -> throw new UnsupportedOperationException("不支持的文档格式: " + fileExtension);
        };
    }

    /**
     * 提取文本文件内容
     */
    private static String extractTextFileContent(Object file, String fileExtension) throws Exception {
        byte[] fileBytes = getFileBytes(file);
        String encoding = detectEncoding(fileBytes);

        String content = new String(fileBytes, encoding);

        // 根据文件类型进行特定处理
        return processTextContent(content, fileExtension);
    }

    /**
     * 提取PDF内容
     */
    private static String extractPdfContent(Object file) throws Exception {
        try (PDDocument document = (file instanceof MultipartFile)
                ? PDDocument.load(((MultipartFile) file).getInputStream())
                : PDDocument.load((File) file)) {

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            return stripper.getText(document);
        }
    }

    /**
     * 提取DOCX内容
     */
    private static String extractDocxContent(Object file) throws Exception {
        try (InputStream is = getInputStream(file);
             XWPFDocument document = new XWPFDocument(is);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {

            return extractor.getText();
        }
    }

    /**
     * 提取DOC内容
     */
    private static String extractDocContent(Object file) throws Exception {
        try (InputStream is = getInputStream(file);
             HWPFDocument document = new HWPFDocument(is);
             WordExtractor extractor = new WordExtractor(document)) {

            return extractor.getText();
        }
    }

    /**
     * 提取Excel内容
     */
    private static String extractExcelContent(Object file) throws Exception {
        StringBuilder content = new StringBuilder();

        try (InputStream is = getInputStream(file);
             Workbook workbook = WorkbookFactory.create(is)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                content.append("=== 工作表: ").append(sheet.getSheetName()).append(" ===\n");

                for (Row row : sheet) {
                    for (Cell cell : row) {
                        String cellValue = getCellValue(cell);
                        content.append(cellValue).append("\t");
                    }
                    content.append("\n");
                }
                content.append("\n");
            }
        }

        return content.toString();
    }

    /**
     * 提取PowerPoint内容（基础实现）
     */
    private static String extractPowerPointContent(Object file) throws Exception {
        // 注意：POI对PPT的文本提取有限，这里返回基本信息
        // 对于完整实现，可能需要使用专门的PPT处理库
        return "[PowerPoint文件] 文件名: " +
                ((file instanceof MultipartFile)
                        ? ((MultipartFile) file).getOriginalFilename()
                        : ((File) file).getName()) +
                "\n注意：PowerPoint文件的文本内容提取需要额外处理库支持。";
    }

    /**
     * 处理文本内容（根据文件类型进行特定处理）
     */
    private static String processTextContent(String content, String fileExtension) {
        // 移除空行和多余空白
        content = content.replaceAll("(?m)^\\s*$", "\n").trim();

        // 对于特定文件类型，可以添加额外的处理逻辑
        return switch (fileExtension) {
            case "csv" -> processCsvContent(content);
            case "xml" -> processXmlContent(content);
            case "json" -> processJsonContent(content);
            case "html", "htm" -> processHtmlContent(content);
            default -> content;
        };
    }

    /**
     * 处理CSV内容 - 格式化显示
     */
    private static String processCsvContent(String content) {
        String[] lines = content.split("\n");
        if (lines.length == 0) return content;

        StringBuilder formatted = new StringBuilder();
        formatted.append("CSV数据 (").append(lines.length).append(" 行):\n");

        // 显示前几行作为示例
        int displayLines = Math.min(lines.length, 10);
        for (int i = 0; i < displayLines; i++) {
            formatted.append("行 ").append(i + 1).append(": ").append(lines[i]).append("\n");
        }

        if (lines.length > displayLines) {
            formatted.append("... 还有 ").append(lines.length - displayLines).append(" 行数据");
        }

        return formatted.toString();
    }

    /**
     * 处理XML内容 - 基础美化
     */
    private static String processXmlContent(String content) {
        // 移除多余空白，简单美化
        return content.replaceAll(">\\s+<", ">\n<")
                .replaceAll("\\s+", " ")
                .trim();
    }

    /**
     * 处理JSON内容 - 基础美化
     */
    private static String processJsonContent(String content) {
        // 简单的JSON格式化
        int indent = 0;
        StringBuilder formatted = new StringBuilder();
        boolean inString = false;

        for (char c : content.toCharArray()) {
            if (c == '"') inString = !inString;
            if (!inString) {
                if (c == '{' || c == '[') {
                    formatted.append(c).append("\n").append(getIndent(++indent));
                } else if (c == '}' || c == ']') {
                    formatted.append("\n").append(getIndent(--indent)).append(c);
                } else if (c == ',') {
                    formatted.append(c).append("\n").append(getIndent(indent));
                } else if (c == ':') {
                    formatted.append(c).append(" ");
                } else {
                    formatted.append(c);
                }
            } else {
                formatted.append(c);
            }
        }

        return formatted.toString();
    }

    /**
     * 处理HTML内容 - 提取文本
     */
    private static String processHtmlContent(String content) {
        // 简单的HTML标签移除
        return content.replaceAll("<[^>]*>", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    /**
     * 获取缩进字符串
     */
    private static String getIndent(int level) {
        return "  ".repeat(Math.max(0, level));
    }

    /**
     * 获取单元格值
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 避免科学计数法
                    double num = cell.getNumericCellValue();
                    if (num == (long) num) {
                        return String.valueOf((long) num);
                    } else {
                        return String.valueOf(num);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    return cell.getCellFormula();
                }
            default:
                return "";
        }
    }

    /**
     * 检测文件编码
     */
    private static String detectEncoding(byte[] bytes) {
        try {
            UniversalDetector detector = new UniversalDetector(null);
            detector.handleData(bytes, 0, Math.min(bytes.length, 10000)); // 只检测前10KB
            detector.dataEnd();
            String encoding = detector.getDetectedCharset();
            detector.reset();
            return encoding != null ? encoding : StandardCharsets.UTF_8.name();
        } catch (Exception e) {
            return StandardCharsets.UTF_8.name();
        }
    }

    /**
     * 获取文件字节数组
     */
    private static byte[] getFileBytes(Object file) throws IOException {
        if (file instanceof MultipartFile) {
            return ((MultipartFile) file).getBytes();
        } else {
            return Files.readAllBytes(((File) file).toPath());
        }
    }

    /**
     * 获取输入流
     */
    private static InputStream getInputStream(Object file) throws IOException {
        if (file instanceof MultipartFile) {
            return ((MultipartFile) file).getInputStream();
        } else {
            return new FileInputStream((File) file);
        }
    }

    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 检查是否支持的文件类型
     */
    public static boolean isSupportedTextFile(String fileExtension) {
        return TEXT_FILE_EXTENSIONS.contains(fileExtension.toLowerCase());
    }

    /**
     * 获取支持的文件扩展名列表
     */
    public static Set<String> getSupportedFileExtensions() {
        return new HashSet<>(TEXT_FILE_EXTENSIONS);
    }

    /**
     * 批量读取文件
     */
    public static List<TextReadResultUtil> readMultipleFiles(List<MultipartFile> files) {
        List<TextReadResultUtil> results = new ArrayList<>();
        for (MultipartFile file : files) {
            results.add(readContent(file));
        }
        return results;
    }

    /**
     * 批量读取本地文件
     */
    public static List<TextReadResultUtil> readMultipleFilesFromPaths(List<Path> filePaths) {
        List<TextReadResultUtil> results = new ArrayList<>();
        for (Path path : filePaths) {
            results.add(readContent(path));
        }
        return results;
    }
}
