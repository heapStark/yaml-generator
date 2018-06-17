package service.result;

/**
 * yamlgen
 * Created by wangzhilei3 on 2018/1/10.
 */
public class OpenApiResult<T> {
    private  T result;
    private String requestId;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OpenApiResult{");
        sb.append("web2.result=").append(result);
        sb.append(", requestId='").append(requestId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
