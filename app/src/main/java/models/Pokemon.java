package models;

public class Pokemon {

    private int id;
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Pokemon (String name, String url){
        this.name=name;
        this.url=url;
    }

    public int getId() {
        String [] urlPartes= url.split("/");
        return Integer.parseInt(urlPartes[urlPartes.length-1]);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(int id) {
        this.id = id;
    }
}
