package admin.test

class TestConfigDomainAdmin {
    static options = [
        list: [ includes: ['name', 'year'] ],
        create: [ includes: ['name', 'year'] ],
        edit: [ includes: ['name', 'year'] ]
    ]
}
