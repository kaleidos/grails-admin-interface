package admin.test

class TestConfigDomainAdmin {
    static options = [
        list: [ includes: ['name', 'year'] ],
        show: [ includes: ['name', 'year'] ],
        edit: [ includes: ['name', 'year'] ]
    ]
}
