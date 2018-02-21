package com.opentable.service;

import java.net.URI;

/**
 * Built with ServiceInfo.Builder, a typical implementation of ServiceInfo
 * The Trust Processor uses this to provide the base information for a ServiceDetails endpoint.
 */
public class ServiceInfoBean implements ServiceInfo {
    private final String serviceName;
    private final String description;
    private final String teamName;
    private final String teamEmail;
    private final String pagerDutyContact;
    private final URI repository;
    private final URI documentation;
    private final URI runBook;
    private final URI metrics;
    private final URI logs;
    private final URI argus;
    private final String chatRoom;

    ServiceInfoBean(final String serviceName, final String description, final String teamName, final String teamEmail, final String pagerDutyContact, final URI repository, final URI documentation, final URI runBook, final URI metrics, final URI logs, final URI argus, final String chatRoom) {
        this.serviceName = serviceName;
        this.description = description;
        this.teamName = teamName;
        this.teamEmail = teamEmail;
        this.pagerDutyContact = pagerDutyContact;
        this.repository = repository;
        this.documentation = documentation;
        this.runBook = runBook;
        this.metrics = metrics;
        this.logs = logs;
        this.argus = argus;
        this.chatRoom = chatRoom;
    }

    @Override
    public String getName() {
        return serviceName;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String teamName() {
        return teamName;
    }

    @Override
    public String teamEmail() {
        return teamEmail;
    }

    @Override
    public String pagerDutyContact() {
        return pagerDutyContact;
    }

    @Override
    public URI repository() {
        return repository;
    }

    @Override
    public URI documentation() {
        return documentation;
    }

    @Override
    public URI runBook() {
        return runBook;
    }

    @Override
    public URI metrics() {
        return metrics;
    }

    @Override
    public URI logs() {
        return logs;
    }

    @Override
    public URI argus() {
        return argus;
    }

    @Override
    public String chatRoom() {
        return chatRoom;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceInfoBean{");
        sb.append("serviceName='").append(serviceName).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", teamName='").append(teamName).append('\'');
        sb.append(", teamEmail='").append(teamEmail).append('\'');
        sb.append(", pagerDutyContact='").append(pagerDutyContact).append('\'');
        sb.append(", repository=").append(repository);
        sb.append(", documentation=").append(documentation);
        sb.append(", runBook=").append(runBook);
        sb.append(", metrics=").append(metrics);
        sb.append(", logs=").append(logs);
        sb.append(", argus=").append(argus);
        sb.append(", chatRoom='").append(chatRoom).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
