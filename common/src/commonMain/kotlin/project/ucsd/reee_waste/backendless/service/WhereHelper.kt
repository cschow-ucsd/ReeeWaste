package project.ucsd.reee_waste.backendless.service

object WhereHelper {
    fun search(
            title: String = "",
            description: String = "",
            type: String = "",
            zipcode: String = "",
            forSaleOnly: Boolean = true
    ): String {
        return "(title like '%$title%' or description like '%$description%') and type like '%$type%'" +
                if (forSaleOnly) " and selling = true" else ""
    }

    fun searchWithOwner(
            title: String = "",
            description: String = "",
            type: String = "",
            zipcode: String = "",
            forSaleOnly: Boolean = false,
            ownerId: String
    ): String = "${search(title, description, type, zipcode, forSaleOnly)} and ownerId = '$ownerId'"
}