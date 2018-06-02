package heap.satrk.yaml.generator.utils;

import heap.satrk.yaml.generator.config.Config;
import heap.satrk.yaml.generator.service.YamlServiceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.6+
 * yamlgen
 * Created by wangzhilei3 on 2018/1/10.
 */
public class YamlServiceUtils {

    public static void genServiceYaml(Config config) throws Exception {
        List<Class> list = new ArrayList<>();
        for (Class c : config.getServiceList()) {
            YamlServiceBean yamlServiceBean = new YamlServiceBean(c, config.getResultPath());
            yamlServiceBean.genYaml();
            list.addAll(yamlServiceBean.getClassList());
        }
        config.setModelList(list);
    }
}
