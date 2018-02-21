/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.opentable.service;

import java.net.URI;
import java.util.Objects;

/**
 * The {@link ServiceInfo} interface is intended for you to declare as a Spring Bean in your application.
 * It can be consumed by our components in order to create things like metric namespaces given information about your
 * service.  You are expected to implement your own implementations of this interface when defining your Bean.
 * The easiest way to do so is use the Builder.
 *
 * For more details on these fields see https://wiki.otcorp.opentable.com/display/CP/The+service-details+Endpoint
 */
public interface ServiceInfo {
    /**
     * Service Name
     */
    String getName();

    /**
     * A description of the service
     */
    String description();

    /**
     * The name of the team responsible for the operation of the service.
     */
    String teamName();

    /**
     * The email of the team
     */
    String teamEmail();

    /**
     * PagerDuty contact info
     */
    String pagerDutyContact();

    /**
     * The github repo wherein the service code resides
     */
    URI repository();

    /**
     * Link to documentation, typically a wiki or README.MD
     */
    URI documentation();

    /**
     * Link to the runbook, typically in the wiki
     */
    URI runBook();

    /**
     * Link to the metrics page in Graphite
     */
    URI metrics();

    /**
     * Link to the kibana dashboard
     */
    URI logs();

    /**
     * Link to argus
     */
    URI argus();

    /**
     * The name of the chat room where this service is discussed
     */
    String chatRoom();

    class Builder {
        private final String serviceName;
        private final String description;
        private String teamName;
        private String teamEmail;
        private String pagerDutyContact;
        private URI repository;
        private URI documentation;
        private URI runBook;
        private URI metrics;
        private URI logs;
        private URI argus;
        private String chatRoom;

        public Builder(String serviceName, String description) {
            Objects.requireNonNull(serviceName);
            Objects.requireNonNull(description);
            this.serviceName = serviceName;
            this.description = description;
        }

        public Builder teamName(final String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Builder teamEmail(final String teamEmail) {
            this.teamEmail = teamEmail;
            return this;
        }

        public Builder pagerDutyContact(final String pagerDutyContact) {
            this.pagerDutyContact = pagerDutyContact;
            return this;
        }

        public Builder repositoryURI(final URI repository) {
            this.repository = repository;
            return this;
        }

        public Builder documentationURI(final URI documentation) {
            this.documentation = documentation;
            return this;
        }

        public Builder runBookURI(final URI runBook) {
            this.runBook = runBook;
            return this;
        }

        public Builder metricsURI(final URI metrics) {
            this.metrics = metrics;
            return this;
        }

        public Builder logsURI(final URI logs) {
            this.logs = logs;
            return this;
        }

        public Builder argusURI(final URI argus) {
            this.argus = argus;
            return this;
        }

        public Builder chatRoomURI(final String chatRoom) {
            this.chatRoom = chatRoom;
            return this;
        }

        public ServiceInfo build() {
            return new ServiceInfoBean(serviceName, description, teamName, teamEmail, pagerDutyContact, repository, documentation, runBook, metrics, logs, argus, chatRoom);
        }
    }
}
