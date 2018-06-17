package heap.stark.yaml.generator.config;


import heap.stark.yaml.generator.utils.ServiceClassBuilder;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private String controllerPackage;
    private String resultPath;
    private String moduleName;

    private List<Class> serviceList;
    private List<Class> modelList;

    public Config(String controllerPackage, String resultPath,String moduleName) {
        this.controllerPackage = controllerPackage;
        this.resultPath = resultPath;
        this.moduleName = moduleName;
        serviceList = new ArrayList<>(new ServiceClassBuilder().getClassByPackageName(controllerPackage)) ;
    }

    @Override
    public String toString() {
        return "Config{" +
                "controllerPackage='" + controllerPackage + '\'' +
                ", resultPath='" + resultPath + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", serviceList=" + serviceList +
                ", modelList=" + modelList +
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

    public List<Class> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Class> serviceList) {
        this.serviceList = serviceList;
    }

    public List<Class> getModelList() {
        return modelList;
    }

    public void setModelList(List<Class> modelList) {
        this.modelList = modelList;
    }
}
