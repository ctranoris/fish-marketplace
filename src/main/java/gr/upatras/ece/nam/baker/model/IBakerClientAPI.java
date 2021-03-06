/**
 * Copyright 2014 University of Patras 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 */

package gr.upatras.ece.nam.baker.model;

import javax.ws.rs.core.Response;

public interface IBakerClientAPI {

	Response getInstalledBunInfoByUUID(String uuid);

	Response getInstalledBuns();

	Response installBun(InstalledBun reqInstallBun);

	Response stopBun(String uuid);

	Response startBun(String uuid);

	Response uninstallBun(String uuid);

	Response reConfigureBun(String uuid);

}
