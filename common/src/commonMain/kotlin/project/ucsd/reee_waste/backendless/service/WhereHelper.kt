package project.ucsd.reee_waste.backendless.service

object WhereHelper {
    fun search(
            title: String = "",
            description: String = "",
            type: String = "",
            zipcode: String = ""
    ): String {
        return "title like '%$title%' and description like '%$description%' and type like '%$type%'"
    }

    fun searchWithOwner(
            title: String = "",
            description: String = "",
            type: String = "",
            zipcode: String = "",
            ownerId: String
    ): String = "${search(title, description, type, zipcode)} and ownerId = '$ownerId'"
}