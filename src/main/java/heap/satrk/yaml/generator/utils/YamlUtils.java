package heap.satrk.yaml.generator.utils;


import heap.satrk.yaml.generator.config.Config;
import heap.satrk.yaml.generator.model.YamlBean;
import heap.satrk.yaml.generator.service.YamlServiceBean;

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
