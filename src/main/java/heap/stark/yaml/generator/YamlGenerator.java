package heap.stark.yaml.generator;

import heap.stark.yaml.generator.config.Config;
import heap.stark.yaml.generator.utils.YamlUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @goal extract
 * @phase package
 * @requiresDependencyResolution test
 */
@Mojo(name = "yaml")
public class YamlGenerator extends AbstractMojo {
    private static final Logger LOGGER = LoggerFactory.getLogger(YamlGenerator.class);
    @Parameter
    private String controllerPackage;
    @Parameter
    private String resultPath;
    @Parameter
    private String moduleName;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Config config = new Config(controllerPackage, resultPath, moduleName);
            config.initByProperties();
            LOGGER.info("config :{}", config);
            YamlUtils.genYaml(config);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

}
