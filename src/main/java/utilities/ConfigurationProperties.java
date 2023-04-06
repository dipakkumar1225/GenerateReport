package utilities;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "file:./config/devices/DevicesDetails.properties",
})
public interface ConfigurationProperties extends Config {

    @Key("ProjectName")
    String getProjectName();

    @Key("Environment")
    String getEnvironment();

    @Key("ExecutionType")
    String getExecutionType();

    @Key("Qualifier")
    String getQualifier();

}
