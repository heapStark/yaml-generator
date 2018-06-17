package heap.stark.yaml.generator.utils;


import heap.stark.yaml.generator.config.Config;
import heap.stark.yaml.generator.model.YamlBean;
import heap.stark.yaml.generator.service.YamlServiceBean;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * yamlgen
 * Created by wangzhilei3 on 2018/1/9.
 *
 * @since 1.6+
 */

public class YamlUtils {

    public static void genYaml(Config config) throws Exception {
        for (Class c : config.getControllerClassSet()) {
            YamlServiceBean yamlServiceBean = new YamlServiceBean(c, config);
            yamlServiceBean.genYaml();
        }
        while (!CollectionUtils.isEmpty(config.getModelClassSet())) {
            Set<Class> classSet = new HashSet<>();
            for (Class c : config.getModelClassSet()) {
                classSet.addAll(new YamlBean(c, config).genYaml());
            }
            config.setModelClassSet(classSet);
        }
    }


}
