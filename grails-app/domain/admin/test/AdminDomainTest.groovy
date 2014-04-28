package admin.test

class AdminDomainTest{

    String name
    Integer age
    String email
    String web

    static constraints = {
        age min:18
        email email:true
        web url:true
    }



}
