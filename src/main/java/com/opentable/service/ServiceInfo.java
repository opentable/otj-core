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

/**
 * The {@link ServiceInfo} interface is intended for you to declare as a Spring Bean in your application.
 * It can be consumed by our components in order to create things like metric namespaces given information about your
 * service.  You are expected to implement your own implementations of this interface when defining your Bean.
 */
public interface ServiceInfo {
    String getName();
}
