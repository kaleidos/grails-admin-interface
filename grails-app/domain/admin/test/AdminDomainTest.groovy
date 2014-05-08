package admin.test

class AdminDomainTest{

    String name
    String surname
    Integer age
    String email
    String web
    Long longNumber
    Integer year
    String country
    Date birthday
//    DateTime lastAccess

    static constraints = {
        name nullable:true
        surname nullable:true, maxSize:100
        age min:18, max:100
        email nullable:true, email:true
        web nullable:true, url:true
        year nullable:true, range:2014..2020
        country nullable:true, inList: ["Canada", "Spain", "USA"]
    }



}
