package osmedile.intellij.stringmanip.encoding;

import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.security.Provider.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* https://stackoverflow.com/a/24983009/685796 */
public class ShowHashAlgorithms {

	private static final void showHashAlgorithms(Provider prov, Class<?> typeClass) {
		String type = typeClass.getSimpleName();

		List<Service> algos = new ArrayList<>();

		Set<Service> services = prov.getServices();
		for (Service service : services) {
			if (service.getType().equalsIgnoreCase(type)) {
				algos.add(service);
			}
		}

		if (!algos.isEmpty()) {
			System.out.printf(" --- Provider %s, version %.2f --- %n", prov.getName(), prov.getVersion());
			for (Service service : algos) {
				String algo = service.getAlgorithm();
				System.out.printf("Algorithm name: \"%s\"%n", algo);

			}
		}

		// --- find aliases (inefficiently)
		Set<Object> keys = prov.keySet();
		for (Object key : keys) {
			final String prefix = "Alg.Alias." + type + ".";
			if (key.toString().startsWith(prefix)) {
				String value = prov.get(key.toString()).toString();
				System.out.printf("Alias: \"%s\" -> \"%s\"%n", key.toString().substring(prefix.length()), value);
			}
		}
	}

	public static void main(String[] args) {
		Provider[] providers = Security.getProviders();
		for (Provider provider : providers) {
			showHashAlgorithms(provider, MessageDigest.class);
		}
	}
}