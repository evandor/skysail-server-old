package de.twenty11.skysail.server.osgi.um.metadata

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.OneToOne
import javax.persistence.CascadeType
import javax.persistence.JoinColumn
import de.twenty11.skysail.server.osgi.um.users.User

@Entity
@Table(name = "um_metadata")
class Metadata extends Serializable {

    @Id
    @Column(name = "metadata_id")
    @GeneratedValue
    @scala.reflect.BeanProperty
    var id = 0

    @OneToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", referencedColumnName="user_id")
    @scala.reflect.BeanProperty
    var userId: User = new User()
    
    @Column
    @scala.reflect.BeanProperty
    var groupId = 0

    @Column
    @scala.reflect.BeanProperty
    var entryPermissions = "-rwxr-----"

}