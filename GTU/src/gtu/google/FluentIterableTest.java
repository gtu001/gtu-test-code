package gtu.google;

import java.util.List;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class FluentIterableTest {
	
	public static void main(String[] args) {
		Predicate<Client> activeClients = new Predicate<Client>() {
			public boolean apply(Client client) {
				return client.activeInLastMonth();
			}
		};
		
		List<Client> list = Lists.newArrayList();
		list.add(new Client("John", "20140316", true));
		// Returns an immutable list of the names of
		// the first 10 active clients in the database.
		List<String> list2 = FluentIterable.from(list)//
				.filter(activeClients) // Predicate
				.transform(Functions.toStringFunction()) // Function
				.limit(10)//
				.toList();
		System.out.println(list2);
		
//		● Chaining (returns FluentIterable) skip,limit, cycle, filter, transform
//		● Querying (returns boolean) allMatch, anyMatch contains, isEmpty
//		● Converting toImmutable{List, Set, SortedSet}  toArray
//		● Extracting first, last, firstMatch (returns Optional) get (returns E)
	}

	static class Client {
		String name;
		String loginTime;
		boolean activeInLastMonth;
		public Client(String name, String loginTime, boolean activeInLastMonth) {
			super();
			this.name = name;
			this.loginTime = loginTime;
			this.activeInLastMonth = activeInLastMonth;
		}
		public boolean activeInLastMonth(){
			return activeInLastMonth;
		}
	}
}
