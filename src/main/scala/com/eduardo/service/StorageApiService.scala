package com.eduardo.service

import scala.collection.JavaConverters.seqAsJavaListConverter

import com.eduardo.util.Resources
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.storage.Storage
import com.google.api.services.storage.model.ObjectAccessControl
import com.google.api.services.storage.model.StorageObject
import com.google.api.services.vision.v1.VisionScopes
import com.google.api.client.http.InputStreamContent
import java.io.InputStream
import com.google.api.services.storage.StorageScopes


class StorageService {

  val BUCKET_UPLOADED = "translationpricerdocs";
//TODO refactor to become single google service creator (there is repeated code here and in storage)  
object StorageService {
  var storage: Option[Storage] = None
  
  /**
   * Returns the Storage API using Application Default Credentials.
   */
  def getStorage(): Storage = {
    
    def buildStorage(): Storage = {
      val appName = Resources.getAppName()
      val credential =
        GoogleCredential.getApplicationDefault().createScoped(StorageScopes.all())
      val jsonFactory = JacksonFactory.getDefaultInstance()
      val built = new Storage.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
        .setApplicationName(appName)
        .build();
      storage = Option(built)
      storage.get
    }
    storage.getOrElse(buildStorage)
  }
  
  def uploadObj(name: String, objType: String, stream: InputStream): Unit = {
    //Build the entity holding the permission - the app itself.
		val entity = "user-"+GoogleCredential.getApplicationDefault().getServiceAccountId;
		val appPermission = new ObjectAccessControl().setEntity(entity).setRole("OWNER")
		//Build obj metadata
    val metadata: StorageObject = new StorageObject().setName(name).setAcl(List(appPermission).asJava)
    val streamContent = new InputStreamContent(objType, stream)
    getStorage().objects().insert(BUCKET_UPLOADED, metadata, streamContent).execute()
  }
  
  def getObj(name: String): StorageObject = {
    getStorage.objects().get(name, BUCKET_UPLOADED).execute()
  }
  
}
  
}
