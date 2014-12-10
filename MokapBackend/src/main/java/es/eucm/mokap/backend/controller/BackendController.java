/**
 *  Copyright [2014] [mokap.es]
 *
 *    This file is part of the mokap community backend (MCB).
 *    MCB is licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package es.eucm.mokap.backend.controller;

import es.eucm.mokap.backend.data.db.DatabaseInterface;
import es.eucm.mokap.backend.data.db.DatastoreAccess;
import es.eucm.mokap.backend.data.storage.CloudStorageAccess;
import es.eucm.mokap.backend.data.storage.StorageInterface;

/**
 * Created by mario on 05/12/2014.
 */
public abstract class BackendController {
	protected static String BUCKET_NAME = System
			.getProperty("backend.BUCKET_NAME");
	protected static final String DOWNLOAD_URL = System
			.getProperty("backend.BASE_URL") + "download?filename=";
	protected static DatabaseInterface db = new DatastoreAccess();
	protected static StorageInterface st = new CloudStorageAccess(BUCKET_NAME,
			DOWNLOAD_URL);
}
