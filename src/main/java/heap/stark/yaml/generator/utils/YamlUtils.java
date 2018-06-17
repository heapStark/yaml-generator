package heap.stark.yaml.generator.utils;


import heap.stark.yaml.generator.config.Config;
import heap.stark.yaml.generator.model.YamlBean;
import heap.stark.yaml.generator.service.YamlServiceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * yamlgen
 * Created by wangzhilei3 on 2018/1/9.
 *
 * @since 1.6+
 */

public class YamlUtils {

    public static void genYaml(Config config) throws Exception {
        List<Class> list = new ArrayList<>();
        for (Class c : config.getServiceList()) {
            YamlServiceBean yamlServiceBean = new YamlServiceBean(c, config);
            yamlServiceBean.genYaml();
            list.addAll(yamlServiceBean.getClassList());
        }
        config.setModelList(list);
        for (Class c : config.getModelList()) {
            new YamlBean(c, config).genYaml();
        }
    }


}
