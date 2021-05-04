package com.krashwani0908.firegram.models

import com.google.firebase.firestore.PropertyName

data class Post(
        @get:PropertyName("Description") @set:PropertyName("Description") var Description:String = "",
        @get:PropertyName("image_url") @set:PropertyName("image_url") var imageUrl:String = "",
        @get:PropertyName("creation_time") @set:PropertyName("creation_time") var creationTime:Long = 0,
        var users:Users?= null
)