package heap.satrk.yaml.generator.config;


import heap.satrk.yaml.generator.utils.ServiceClassBuilder;

import java.util.List;

public class Config {
    private String controllerPackage;
    private String resultPath;

    private List<Class> serviceList;
    private List<Class> modelList;

    public Config(String controllerPackage, String resultPath) {
        this.controllerPackage = controllerPackage;
        this.resultPath = resultPath;
        serviceList = new ServiceClassBuilder().getClassByPackageName(controllerPackage);
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

    @Override
    public String toString() {
        return "Config{" +
                "controllerPackage='" + controllerPackage + '\'' +
                ", resultPath='" + resultPath + '\'' +
                ", serviceList=" + serviceList +
                ", modelList=" + modelList +
                '}';
    }
}
