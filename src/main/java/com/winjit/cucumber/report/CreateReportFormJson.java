package com.winjit.cucumber.report;

import lombok.SneakyThrows;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.reducers.ReducingMethod;
import net.masterthought.cucumber.sorting.SortingMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import utilities.ConfigurationManager;
import utilities.ConfigurationProperties;
import utilities.GenerateSummary;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateReportFormJson {

    public CreateReportFormJson() {
    }

    private final String PATTERN = "yyyy_MM_dd HH_mm_ss";
    private final Date CURRENT_DATE = new Date();
    private final String REPORT_DIR = System.getProperty("user.dir") + File.separator + "Reports_";

    @SneakyThrows
    public void getReport() {

        ConfigurationProperties configurationProperties = ConfigurationManager.getConfiguration();
        String jsonFilesDir = System.getProperty("user.dir") + File.separator + "Cucumber_Json_Files";
        Collection<File> jsonFiles = FileUtils.listFiles(new File(jsonFilesDir), new String[]{"json"}, true);
        List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));

        String buildNumber = "01";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
        String formatted = simpleDateFormat.format(CURRENT_DATE);
        String strReportDir = REPORT_DIR + formatted;
        Configuration configuration = new Configuration(new File(strReportDir), configurationProperties.getProjectName());
        configuration.setBuildNumber(buildNumber);

        configuration.setSortingMethod(SortingMethod.NATURAL);
        configuration.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);
        configuration.addReducingMethod(ReducingMethod.HIDE_EMPTY_HOOKS);
        configuration.addPresentationModes(PresentationMode.PARALLEL_TESTING);

        Set<Status> setStatus = new HashSet<>();
        setStatus.add(Status.SKIPPED);
        setStatus.add(Status.PENDING);
        setStatus.add(Status.UNDEFINED);
        configuration.setNotFailingStatuses(setStatus);

        jsonFiles.forEach(file -> {
                    String strFileName = FilenameUtils.removeExtension(file.getName());
                    configuration.setQualifier(strFileName, configurationProperties.getQualifier());
                }
        );

        List<String> classificationFiles = new ArrayList<>();
        configuration.addClassifications("Environment", "UAT");
        configuration.addClassifications("Execution Type", configurationProperties.getEnvironment().toUpperCase() + " - " + configurationProperties.getExecutionType());
        configuration.addClassificationFiles(classificationFiles);
        // points to the demo trends which is not used for other tests
        /*configuration.setTrendsStatsFile(new File("target/test-classes/demo-trends.json"));

        configuration.addCustomCssFiles(Collections.singletonList("src/test/resources/css/stackoverflow-light.min.css"));
        configuration.addCustomJsFiles(Arrays.asList("src/test/resources/js/enable-highlighting.js", "src/test/resources/js/highlight.min.js"));*/

        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, configuration);
        reportBuilder.generateReports();
    }

    public void createEmailAbleReport(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
        String formatted = simpleDateFormat.format(CURRENT_DATE);
        String strReportDir = REPORT_DIR + formatted;

        String inputHTML = Paths.get(strReportDir + File.separator + "cucumber-html-reports" + File.separator + "overview-features.html").toAbsolutePath().toString();
        File fileEmailAbleReport = new File(strReportDir, "overview-features-emailable-report.html");
        try {
            FileUtils.writeStringToFile(fileEmailAbleReport, GenerateSummary.parseSummaryReport(inputHTML).toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles())).parallel()
                .filter(file -> !file.isDirectory())
                .map(File::getAbsolutePath)
                .collect(Collectors.toSet());
    }

    public static void main(String... str) {
        CreateReportFormJson createReportFormJson = new CreateReportFormJson();
        createReportFormJson.getReport();
        createReportFormJson.createEmailAbleReport();
    }
}
