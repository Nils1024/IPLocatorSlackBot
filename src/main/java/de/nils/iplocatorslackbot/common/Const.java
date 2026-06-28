package de.nils.iplocatorslackbot.common;

public class Const {
    public class Commands {
        public static final String HELP = "/ipl-help";
        public static final String IP_LOOKUP =  "/ip-lookup";
        public static final String DOMAIN_LOOKUP =  "/domain-lookup";
        public static final String TLD_LOOKUP =  "/tld-lookup";
        public static final String ASN_LOOKUP =  "/asn-lookup";
    }

    public class API {
        public static final String BASE_API_URL = "https://ipapi.nilsb.tech/v1/";
        public static final String IP_API_URL = BASE_API_URL + "ip/";
        public static final String DOMAIN_API_URL = BASE_API_URL + "domain/";
        public static final String TLD_API_URL = BASE_API_URL + "tld/";
        public static final String ASN_API_URL = BASE_API_URL + "asn/";
    }
}
