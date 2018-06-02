package heap.satrk.yaml.generator.utils;


import heap.satrk.yaml.generator.config.Config;
import heap.satrk.yaml.generator.model.YamlBean;

/**
 * yamlgen
 * Created by wangzhilei3 on 2018/1/9.
 *
 * @since 1.6+
 */

public class YamlUtils {

    public static void genModelYaml(Config config) throws Exception {
        for (Class c : config.getModelList()) {
            new YamlBean(c, config.getResultPath()).genYaml();
        }
    }


}
