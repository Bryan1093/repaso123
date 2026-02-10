package uce.edu.web.api.agenda.interfaces;

import java.util.ArrayList;
import java.util.List;

public class EntityResponse<T> {
    private T data;
    private List<Link> links;

    public EntityResponse(T data) {
        this.data = data;
        this.links = new ArrayList<>();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(String rel, String href, String method) {
        this.links.add(new Link(rel, href, method));
    }
}
