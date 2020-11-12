import org.junit.Assert;
import org.junit.Test;
import java.util.List;
public class AddressBookTest {

    @Test
    public void MatchContactCount() {
        AddressBookSystem addressBookSystem = new AddressBookSystem();
        List<Contact> addressBookContactList = addressBookSystem.readAddressBookData( AddressBookSystem.IOService.DB_IO );
        System.out.println(addressBookContactList);
        Assert.assertEquals(7,addressBookContactList.size());
    }

    @Test
    public void givenNewZipForContactSyncWithDataBase() {
        AddressBookSystem addressBookSystem = new AddressBookSystem();
        List<Contact> employeePayrollData = addressBookSystem.readAddressBookData(AddressBookSystem.IOService.DB_IO);
        addressBookSystem.updateContactPhoneNo("Sai",503692);
        boolean result = addressBookSystem.checkAddressBookInsyncWithDB("Sai");
        Assert.assertTrue( result );
    }
    @Test
    public void givenAddressBookContactData_WhenRetrieveByCity_ShouldReturnProperValue() {
        AddressBookSystem addressBookSystem = new AddressBookSystem();
        addressBookSystem.readAddressBookData(DB_IO);
        Map<String,Integer> countContactByCity = addressBookSystem.readContactCountByCity(DB_IO);
        Assert.assertTrue( countContactByCity.get("Mahabubnagar").equals( 2 )
                && countContactByCity.get( "Hosur" ).equals( 1 ) && countContactByCity.get( "Kurnool" ).equals( 1 ) && countContactByCity.get( "Kadapa" ).equals( 1 ));
    }
    @Test
    public void givenNewContact_WhenAdded_ShouldSyncWithDB() {
        AddressBookSystem addressBookSystem = new AddressBookSystem();
        addressBookSystem.readAddressBookData(DB_IO);
        addressBookSystem.addContactToAddressBook("ab","Bunny","Balajinagar","vizag","AP",Long.parseLong( "92512"),Long.parseLong( "9869854127" ),"xyz@gmail.com","family",LocalDate.now());
        boolean result = addressBookSystem.checkAddressBookInsyncWithDB("ab");
        Assert.assertTrue( result );
    }
    @Test
    public void given6Contact_WhenAddedToDB_ShouldMatchContactEntries() {
        Contact[] arrayOfContacts = {
                new Contact( "Ramu" ,"Bezos","Balajinagar","vizag","Telangana",
                        Long.parseLong( "51512"),Long.parseLong( "9869857427" ),"lol@gmail.com","family",LocalDate.now()),
                new Contact( "sai","krish","BagirathaColony","hyderabad","AP",
                        Long.parseLong( "41512"),Long.parseLong( "8969854217" ),"poi@gmail.com","friend",LocalDate.now()),
                new Contact( "krish","krishna","padmavathicolony","karimnagar","Tamilnadu",
                        Long.parseLong( "41512"),Long.parseLong( "6899854172" ),"efg@gmail.com","family",LocalDate.now()),
                new Contact( "vamsi","reddy","BNcolony","vijawada","Telangana",
                        Long.parseLong( "92541"),Long.parseLong( "85698541213" ),"bcd@gmail.com","family",LocalDate.now()),
                new Contact( "Mukesh","singh","BKreddy","vizag","AP",
                        Long.parseLong( "85112"),Long.parseLong( "69598544157" ),"abc@gmail.com","friend",LocalDate.now()),
                new Contact( "bibhav","singh","RRKclny","Kurnool","AP",
                        Long.parseLong( "74112"),Long.parseLong( "7899854127" ),"akh@gmail.com","friend",LocalDate.now()),
        };
        AddressBookSystem addressBookSystem = new AddressBookSystem();
        addressBookSystem.readAddressBookData( DB_IO );
        Instant start = Instant.now();
        addressBookSystem.addContactsToAddressBook( Arrays.asList(arrayOfContacts));
        Instant end = Instant.now();
        System.out.println("Duration without Thread: "+ Duration.between( start,end ) );
        Instant threadStart = Instant.now();
        addressBookSystem.addContactsToAddressBookWithThreads(Arrays.asList(arrayOfContacts));
        Instant threadend = Instant.now();
        System.out.println("Duration with thread: "+Duration.between( threadStart,threadend ));
        Assert.assertEquals( 17,addressBookSystem.countEntries(DB_IO) );
    }

}