package heap.satrk.yaml.generator;

import heap.satrk.yaml.generator.config.Config;
import heap.satrk.yaml.generator.utils.YamlServiceUtils;
import heap.satrk.yaml.generator.utils.YamlUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @goal extract
 * @phase package
 * @requiresDependencyResolution test
 */
@Mojo(name = "yaml")
public class YamlGenerator extends AbstractMojo {
    @Parameter
    private String controllerPackage;
    @Parameter
    private String resultPath;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Config config = new Config(controllerPackage, resultPath);
            YamlServiceUtils.genServiceYaml(config);
            YamlUtils.genModelYaml(config);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }

}
