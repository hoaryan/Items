import grails.util.Environment // Für Hibernate Database Explorer s.u.
import grails.util.GrailsUtil // Ebenfalls Environment spezifische Konfig nutzen
import org.apache.commons.codec.digest.DigestUtils  // für MD5 der Passwörter
import java.text.SimpleDateFormat
import java.text.DateFormat



class BootStrap {

    def defaultHomeItems = [ "Todo","Next","Projects","Maybe/Sometimes","Locations","Contacts" ];

     def init = { servletContext ->
        log.info "================ Init Bootstrap "

        switch(GrailsUtil.environment) {
            case "development":
            bootstrapDevEnvironment()
            break
        case "production":
            bootstrapProdEnvironment()
            break
        }

        log.info "================ Bootstrap... finished\n\n "

        // Hibernate Database Explorer laden: ("devDB")
        //  org.hsqldb.util.DatabaseManager.main()

    }

    def bootstrapProdEnvironment() {
        log.info "currently doing nothing at all.."
    }

    def bootstrapDevEnvironment() {
        
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy")
        
        Date einBeliebigesDatum = formatter.parse("08-01-2010")
        Date einWeiteresBeliebigesDatum = formatter.parse("02-01-2010")


        def userJones = new User(email: "schwarz.johannes@gmx.de", lastName:"Schwarz", firstName:"Johannes", password:DigestUtils.md5Hex("test")).save()
        def homeJones = new Item(name:"Home", user:userJones).save()
        def inboxJones = new Item(name:"Inbox", user:userJones, itemType:Item.ITEM_INBOX).save()
        inboxJones.addToTags(homeJones)
        def logbookJones= new Item(name:"Logbook", user:userJones, itemType:Item.ITEM_LOGBOOK).save()
        logbookJones.addToTags(homeJones)
        userJones.homeItemID = homeJones.id
        userJones.inboxItemID = inboxJones.id
        userJones.logbookItemID = logbookJones.id

        def userChris = new User(email: "christian.groneberg@googlemail.com", lastName:"Groneberg", firstName:"Christian", password:DigestUtils.md5Hex("test"), isAdmin:true).save()
        def homeChris = new Item(name:"Home", user:userChris).save()
        def inboxChris = new Item(name:"Inbox", user:userChris, itemType:Item.ITEM_INBOX).save()
        inboxChris.addToTags(homeChris)
        def logbookChris = new Item(name:"Logbook", user:userChris, itemType:Item.ITEM_LOGBOOK).save()
        logbookChris.addToTags(homeChris)
        userChris.homeItemID = homeChris.id
        userChris.inboxItemID = inboxChris.id
        userChris.logbookItemID = logbookChris.id

        def userHorst = new User(email: "horst.sueggel@googlemail.com", lastName:"Süggel", firstName:"Horst", password:DigestUtils.md5Hex("test"), isAdmin:true).save()
        def homeHorst = new Item(name:"Home", user:userHorst).save()
        def inboxHorst = new Item(name:"Inbox", user:userHorst, itemType:Item.ITEM_INBOX).save()
        inboxHorst.addToTags(homeHorst)
        def logbookHorst = new Item(name:"Logbook", user:userHorst, itemType:Item.ITEM_LOGBOOK).save()
        logbookHorst.addToTags(homeHorst)
        userHorst.homeItemID = homeHorst.id
        userHorst.inboxItemID = inboxHorst.id
        userHorst.logbookItemID = logbookHorst.id


        // Jones TestItems

        def glueh = new Item(name: "Glühbirne", user: userJones).save()
        def bayr = new Item(name: "Bayrischzell", user: userJones).save()
        def obi = new Item(name: "Obi", user: userJones).save()
        def jtodo = new Item(name:"ToDo1", user: userJones).save()

        jtodo.addToItemDates(new ItemDate(startDate:new Date()))
        bayr.addToItemDates(new ItemDate(startDate:einWeiteresBeliebigesDatum, endDate:einBeliebigesDatum))

        bayr.addToTags(homeJones)
        glueh.addToTags(obi)
        glueh.addToTags(bayr)
        glueh.save()
        def schrauben = new Item(name:"Schrauben kaufen", user: userJones)
        schrauben.addToTags(obi)
        schrauben.save()
        jtodo.addToChildren(obi)
        homeJones.addToChildren(jtodo)


        // Groneberg TestItems
        createDefaultHomeItems(homeChris, userChris)


//        // Horst TestItems
        createDefaultHomeItems(homeHorst, userHorst)

        DateFormat formatterWithTime = new SimpleDateFormat("dd-MM-yyyy HH:mm")

        Date date1 = formatterWithTime.parse('03-01-2010 20:15')
        Date date2 = formatterWithTime.parse('03-01-2010 22:05')
        Date date3 = formatter.parse('05-01-2010')
        Date date4 = formatterWithTime.parse('06-01-2010 10:20')
        Date date5 = formatterWithTime.parse('07-01-2010 08:00')
        Date date6 = formatterWithTime.parse('08-01-2010 09:00')

        def realEk = new Item(name: "Real", notes: "Einkaufsliste", user: userHorst).save()
        realEk.addToItemDates(new ItemDate(startDate:einWeiteresBeliebigesDatum, endDate:einBeliebigesDatum))

        def Brot = new Item(name: "Brot", user: userHorst).save()
        Brot.addToItemDates(new ItemDate(startDate:date1, endDate:date2, isGanztaegig:false))

        def Schinken = new Item(name: "Schinken", user: userHorst).save()
        Schinken.addToItemDates(new ItemDate(startDate:date3, isGanztaegig:true))

        def Hanuta = new Item(name: "Hanuta", user: userHorst).save()
        Hanuta.addToItemDates(new ItemDate(startDate:date4, isGanztaegig:false))

        def Eier = new Item(name: "Eier", user: userHorst).save()
        Eier.addToItemDates(new ItemDate(startDate:date5, endDate:date6, isGanztaegig:false))

        def Katzeklo = new Item(name: "Katzeklo", user: userHorst).save()
        def Bergbauern = new Item(name: "Bergbauern Milch", user: userHorst).save()
        def Wein = new Item(name: "Wein für Candle-Light Dinner", user: userHorst).save()

        realEk.addToChildren(Brot)
        realEk.addToChildren(Schinken)
        realEk.addToChildren(Hanuta)
        realEk.addToChildren(Eier)
        realEk.addToChildren(Katzeklo)
        realEk.addToChildren(Bergbauern)
        realEk.addToChildren(Wein)

        def horstTodoItem = new Item(name:"Todos und so... =)", user: userHorst).save()
        horstTodoItem.addToItemDates(new ItemDate(startDate:new Date()))

        realEk.addToTags(horstTodoItem)

        //list 1
        def list1Item1 = new Item(name: "Item1", user: userHorst).save()
        def list1Item2 = new Item(name: "Item2", user: userHorst).save()
        def list1Item3 = new Item(name: "Item3", user: userHorst).save()
        def list1Item4 = new Item(name: "Item4", user: userHorst).save()
        horstTodoItem.addToChildren(list1Item3)
        horstTodoItem.addToChildren(list1Item4)

        def list1 = new Item(name: "Children both", user: userHorst).save()
        list1.addToChildren(list1Item1);
        list1.addToChildren(list1Item2);
        list1.addToChildren(list1Item3);
        list1.addToChildren(list1Item4);

        //list 2
        def list2Item1 = new Item(name: "1 Tag", user: userHorst).save()
        def list2Item2 = new Item(name: "2 Tags", user: userHorst).save()
        def list2Item3 = new Item(name: "3 Tags", user: userHorst).save()
        def list2Item4 = new Item(name: "4 Tags", user: userHorst).save()


        def list2 = new Item(name: "TagSortOrder", user: userHorst).save()
        list2.addToChildren(list2Item1);
        list2.addToChildren(list2Item2);
        list2.addToChildren(list2Item3);
        list2.addToChildren(list2Item4);

        list1Item1.addToChildren(list2Item1)
        list1Item1.addToChildren(list2Item2)
        list1Item2.addToChildren(list2Item2)
        list1Item1.addToChildren(list2Item3)
        list1Item2.addToChildren(list2Item3)
        list1Item3.addToChildren(list2Item3)
        list1Item1.addToChildren(list2Item4)
        list1Item2.addToChildren(list2Item4)
        list1Item3.addToChildren(list2Item4)
        list1Item4.addToChildren(list2Item4)

        homeHorst.addToChildren(realEk)
        homeHorst.addToChildren(horstTodoItem)
        homeHorst.addToChildren(list1)
        homeHorst.addToChildren(list2)


    }


    void createDefaultHomeItems(Item homeItem, User user){
        defaultHomeItems.each(){
            def tempItem = new Item(name:it, user: user).save()
            homeItem.addToChildren(tempItem)
        }
    }

     def destroy = {
     }
} 