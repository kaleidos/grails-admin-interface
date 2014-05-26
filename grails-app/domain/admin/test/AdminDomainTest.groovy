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
    Boolean ok
    File photo
    Locale locale
    Date lastAccess

    static mapping = {
        id generator:'assigned'
    }

    static constraints = {
        id nullable: false, bindable: true
        name nullable:true
        surname nullable:true, maxSize:100
        age min:18, max:100
        email nullable:true, email:true
        web nullable:true, url:true
        year nullable:true, range:2014..2020
        country nullable:true, inList: ["Canada", "Spain", "USA"]
        ok nullable:true
        photo nullable:true
        locale nullable:true
        lastAccess nullable:true
    }



}
