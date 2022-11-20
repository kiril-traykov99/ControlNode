package com.nbu.controlnode.datanode;

public class DockerDataNodeEndpoint {

    String contactInfo;

    public DockerDataNodeEndpoint(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getContactInfo() {
        return contactInfo;
    }

}
