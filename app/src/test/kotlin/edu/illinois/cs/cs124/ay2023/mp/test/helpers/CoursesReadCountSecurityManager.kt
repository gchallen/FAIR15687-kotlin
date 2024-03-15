package edu.illinois.cs.cs124.ay2023.mp.test.helpers

import java.nio.file.Path
import java.security.Permission

/*
* Used to count the number of times that the API server accesses courses.json, to fail
* implementations that repeatedly parse the file after Server initialization.
*
* Here we utilize the fact that Java's (deprecated) security architecture allows us to record
* file accesses to courses.json.
* Normally you'd use this to perform access control, but we can also use it to simply count
* the number of times a file was accessed.
*/
class CoursesReadCountSecurityManager : SecurityManager() {
    private var coursesPath = Path.of(
        CoursesReadCountSecurityManager::class.java.getResource("/courses.json")!!.toURI(),
    )
    var coursesReadCount = 0
        private set

    override fun checkPermission(perm: Permission) {}
    override fun checkPermission(perm: Permission, context: Any) {}
    override fun checkRead(file: String) {
        if (Path.of(file) == coursesPath) {
            coursesReadCount++
        }
        super.checkRead(file)
    }

    override fun checkRead(file: String, context: Any) {
        if (Path.of(file) == coursesPath) {
            coursesReadCount++
        }
        super.checkRead(file, context)
    }
}
// md5: b5012d340f1fcb8ffbdb9bccb775fafc // DO NOT REMOVE THIS LINE
