package heap.stark.yaml.generator.config;


import heap.stark.yaml.generator.utils.ServiceClassBuilder;

import java.util.HashSet;
import java.util.Set;

public class Config {
    private String controllerPackage;
    private String resultPath;
    private String moduleName;

    private Set<Class> controllerClassSet;
    private Set<Class> modelClassSet;

    public Config(String controllerPackage, String resultPath, String moduleName) {
        this.controllerPackage = controllerPackage;
        this.resultPath = resultPath;
        this.moduleName = moduleName;
        controllerClassSet = new ServiceClassBuilder().getClassByPackageName(controllerPackage);
        this.modelClassSet = new HashSet<>();
    }

    @Override
    public String toString() {
        return "Config{" +
                "controllerPackage='" + controllerPackage + '\'' +
                ", resultPath='" + resultPath + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", controllerClassSet=" + controllerClassSet +
                ", modelClassList=" + modelClassSet +
                '}';
    }

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Set<Class> getControllerClassSet() {
        return controllerClassSet;
    }

    public void setControllerClassSet(Set<Class> controllerClassSet) {
        this.controllerClassSet = controllerClassSet;
    }

    public Set<Class> getModelClassSet() {
        return modelClassSet;
    }

    public void setModelClassSet(Set<Class> modelClassSet) {
        this.modelClassSet = modelClassSet;
    }
}
