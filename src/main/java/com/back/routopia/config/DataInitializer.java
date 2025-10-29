package com.back.routopia.config;

import com.back.routopia.entity.*;
import com.back.routopia.repositroy.DestinoRespository;
import com.back.routopia.repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DestinoRespository destinoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuario administrador por defecto si no existe
        if (!userRepository.existsByEmail("admin@routopia.com")) {
            User admin = new User();
            admin.setNombre("Admin");
            admin.setApellido("Routopia");
            admin.setEmail("admin@routopia.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            System.out.println("Admin user created:");
            System.out.println("Email: admin@routopia.com");
            System.out.println("Password: admin123");
        }

        // Crear usuario normal de ejemplo si no existe
        if (!userRepository.existsByEmail("user@routopia.com")) {
            User user = new User();
            user.setNombre("Usuario");
            user.setApellido("Demo");
            user.setEmail("user@routopia.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            userRepository.save(user);

            System.out.println("Normal user created:");
            System.out.println("Email: user@routopia.com");
            System.out.println("Password: user123");
        }

        // Crear destinos de ejemplo si no existen
        createSampleDestinos();
    }

    private void createSampleDestinos() {
        if (destinoRepository.count() == 0) {
            System.out.println("Creating sample destinations...");

            // Destino 1: Torre Eiffel - Francia
            if (!destinoRepository.existsByNameIgnoreCase("Torre Eiffel")) {
                Destino destino1 = new Destino();
                destino1.setName("Torre Eiffel");
                destino1.setPrecio(25.0f);
                destino1.setDuration_time("2-3 horas");
                destino1.setDescription("La Torre Eiffel es una torre de hierro pudelado de 324 metros de altura situada en París. Construida en 1889, es el símbolo más reconocible de Francia y uno de los monumentos más visitados del mundo.");
                destino1.setCategory(Category.FRANCE);
                destino1.setAddress("Champ de Mars, 5 Avenue Anatole France, 75007 París, Francia");
                destino1.setPunctuation(4.8f);
                destino1.setCity("París");
                destino1.setImageUrl("https://images.unsplash.com/photo-1511739001486-6bfe10ce785f?w=800&h=600&fit=crop");
                destino1.setSecondaryImages(Arrays.asList(
                    "https://images.unsplash.com/photo-1502602898536-47ad22581b52?w=400&h=300&fit=crop",
                    "https://images.unsplash.com/photo-1549144511-f099e773c147?w=400&h=300&fit=crop"
                ));
                destino1.setLanguages(new HashSet<>(Arrays.asList(Language.FRENCH, Language.ENGLISH)));
                destinoRepository.save(destino1);
            }

            // Destino 2: Museo del Louvre - Francia
            if (!destinoRepository.existsByNameIgnoreCase("Museo del Louvre")) {
                Destino destino2 = new Destino();
                destino2.setName("Museo del Louvre");
                destino2.setPrecio(17.0f);
                destino2.setDuration_time("4-6 horas");
                destino2.setDescription("El Museo del Louvre es el museo de arte más grande del mundo y un monumento histórico en París. Hogar de la Mona Lisa y miles de obras maestras de la humanidad.");
                destino2.setCategory(Category.FRANCE);
                destino2.setAddress("Rue de Rivoli, 75001 París, Francia");
                destino2.setPunctuation(4.7f);
                destino2.setCity("París");
                destino2.setImageUrl("https://images.unsplash.com/photo-1566139884350-dfe0b4b5b5b8?w=800&h=600&fit=crop");
                destino2.setSecondaryImages(Arrays.asList(
                    "https://images.unsplash.com/photo-1541961017774-22349e4a1262?w=400&h=300&fit=crop",
                    "https://images.unsplash.com/photo-1605721911519-3dfeb3be25e7?w=400&h=300&fit=crop"
                ));
                destino2.setLanguages(new HashSet<>(Arrays.asList(Language.FRENCH, Language.ENGLISH, Language.SPANISH)));
                destinoRepository.save(destino2);
            }

            // Destino 3: Templo Senso-ji - Japón
            if (!destinoRepository.existsByNameIgnoreCase("Templo Senso-ji")) {
                Destino destino3 = new Destino();
                destino3.setName("Templo Senso-ji");
                destino3.setPrecio(0.0f);
                destino3.setDuration_time("1-2 horas");
                destino3.setDescription("El templo más antiguo de Tokio, fundado en el año 628. Un lugar sagrado dedicado a la diosa budista Kannon, rodeado de jardines tradicionales y mercados históricos.");
                destino3.setCategory(Category.JAPAN);
                destino3.setAddress("2 Chome-3-1 Asakusa, Taito City, Tokio 111-0032, Japón");
                destino3.setPunctuation(4.6f);
                destino3.setCity("Tokio");
                destino3.setImageUrl("https://images.unsplash.com/photo-1545569341-9eb8b30979d9?w=800&h=600&fit=crop");
                destino3.setSecondaryImages(Arrays.asList(
                    "https://images.unsplash.com/photo-1528164344705-47542687000d?w=400&h=300&fit=crop",
                    "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=400&h=300&fit=crop"
                ));
                destino3.setLanguages(new HashSet<>(Arrays.asList(Language.ENGLISH)));
                destinoRepository.save(destino3);
            }

            // Destino 4: Monte Fuji - Japón
            if (!destinoRepository.existsByNameIgnoreCase("Monte Fuji")) {
                Destino destino4 = new Destino();
                destino4.setName("Monte Fuji");
                destino4.setPrecio(50.0f);
                destino4.setDuration_time("1 día completo");
                destino4.setDescription("El monte más alto de Japón con 3,776 metros. Un volcán activo y símbolo nacional, perfecto para senderismo y contemplar paisajes únicos. Patrimonio de la Humanidad por la UNESCO.");
                destino4.setCategory(Category.JAPAN);
                destino4.setAddress("Kitayama, Fujinomiya, Shizuoka 418-0112, Japón");
                destino4.setPunctuation(4.9f);
                destino4.setCity("Fujinomiya");
                destino4.setImageUrl("https://images.unsplash.com/photo-1490806843957-31f4c9a91c65?w=800&h=600&fit=crop");
                destino4.setSecondaryImages(Arrays.asList(
                    "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400&h=300&fit=crop",
                    "https://images.unsplash.com/photo-1513407030348-c983a97b98d8?w=400&h=300&fit=crop"
                ));
                destino4.setLanguages(new HashSet<>(Arrays.asList(Language.ENGLISH)));
                destinoRepository.save(destino4);
            }

            // Destino 5: Acrópolis de Atenas - Grecia
            if (!destinoRepository.existsByNameIgnoreCase("Acrópolis de Atenas")) {
                Destino destino5 = new Destino();
                destino5.setName("Acrópolis de Atenas");
                destino5.setPrecio(20.0f);
                destino5.setDuration_time("3-4 horas");
                destino5.setDescription("Ciudadela antigua situada en una cima rocosa sobre Atenas. Hogar del Partenón y otros templos clásicos, representa la cuna de la democracia y la civilización occidental.");
                destino5.setCategory(Category.GREECE);
                destino5.setAddress("Atenas 105 58, Grecia");
                destino5.setPunctuation(4.5f);
                destino5.setCity("Atenas");
                destino5.setImageUrl("https://images.unsplash.com/photo-1555993539-1732b0258235?w=800&h=600&fit=crop");
                destino5.setSecondaryImages(Arrays.asList(
                    "https://images.unsplash.com/photo-1576485290814-1c72aa4bbb8e?w=400&h=300&fit=crop",
                    "https://images.unsplash.com/photo-1539650116574-75c0c6d73f6e?w=400&h=300&fit=crop"
                ));
                destino5.setLanguages(new HashSet<>(Arrays.asList(Language.ENGLISH, Language.SPANISH)));
                destinoRepository.save(destino5);
            }

            // Destino 6: Santorini - Grecia
            if (!destinoRepository.existsByNameIgnoreCase("Santorini")) {
                Destino destino6 = new Destino();
                destino6.setName("Santorini");
                destino6.setPrecio(80.0f);
                destino6.setDuration_time("2-3 días");
                destino6.setDescription("Isla volcánica en el mar Egeo famosa por sus puestas de sol espectaculares, arquitectura cicládica blanca y azul, y vinos únicos. Un paraíso romántico y fotogénico.");
                destino6.setCategory(Category.GREECE);
                destino6.setAddress("Santorini, Grecia");
                destino6.setPunctuation(4.8f);
                destino6.setCity("Fira");
                destino6.setImageUrl("https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?w=800&h=600&fit=crop");
                destino6.setSecondaryImages(Arrays.asList(
                    "https://images.unsplash.com/photo-1613395877344-13d4a8e0d49e?w=400&h=300&fit=crop",
                    "https://images.unsplash.com/photo-1533105079780-92b9be482077?w=400&h=300&fit=crop"
                ));
                destino6.setLanguages(new HashSet<>(Arrays.asList(Language.ENGLISH, Language.SPANISH)));
                destinoRepository.save(destino6);
            }

            // Destino 7: Templos de Angkor - Tailandia (ajustado a categoría disponible)
            if (!destinoRepository.existsByNameIgnoreCase("Palacio Real de Bangkok")) {
                Destino destino7 = new Destino();
                destino7.setName("Palacio Real de Bangkok");
                destino7.setPrecio(15.0f);
                destino7.setDuration_time("2-3 horas");
                destino7.setDescription("Complejo de edificios que ha sido la residencia oficial de los reyes de Tailandia desde 1782. Arquitectura tailandesa tradicional con templos dorados y jardines exuberantes.");
                destino7.setCategory(Category.THAILAND);
                destino7.setAddress("Phra Nakhon, Bangkok 10200, Tailandia");
                destino7.setPunctuation(4.4f);
                destino7.setCity("Bangkok");
                destino7.setImageUrl("https://images.unsplash.com/photo-1528181304800-259b08848526?w=800&h=600&fit=crop");
                destino7.setSecondaryImages(Arrays.asList(
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop",
                    "https://images.unsplash.com/photo-1552465011-b4e21bf6e79a?w=400&h=300&fit=crop"
                ));
                destino7.setLanguages(new HashSet<>(Arrays.asList(Language.ENGLISH)));
                destinoRepository.save(destino7);
            }

            // Destino 8: Islas Phi Phi - Tailandia
            if (!destinoRepository.existsByNameIgnoreCase("Islas Phi Phi")) {
                Destino destino8 = new Destino();
                destino8.setName("Islas Phi Phi");
                destino8.setPrecio(45.0f);
                destino8.setDuration_time("1 día completo");
                destino8.setDescription("Archipiélago de seis islas en el mar de Andamán. Aguas cristalinas, playas de arena blanca, acantilados de piedra caliza y vida marina espectacular. Perfecto para snorkel y buceo.");
                destino8.setCategory(Category.THAILAND);
                destino8.setAddress("Islas Phi Phi, Krabi, Tailandia");
                destino8.setPunctuation(4.7f);
                destino8.setCity("Krabi");
                destino8.setImageUrl("https://images.unsplash.com/photo-1552465011-b4e21bf6e79a?w=800&h=600&fit=crop");
                destino8.setSecondaryImages(Arrays.asList(
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop",
                    "https://images.unsplash.com/photo-1528181304800-259b08848526?w=400&h=300&fit=crop"
                ));
                destino8.setLanguages(new HashSet<>(Arrays.asList(Language.ENGLISH)));
                destinoRepository.save(destino8);
            }

            // Destino 9: Chichen Itzá - México
            if (!destinoRepository.existsByNameIgnoreCase("Chichen Itzá")) {
                Destino destino9 = new Destino();
                destino9.setName("Chichen Itzá");
                destino9.setPrecio(30.0f);
                destino9.setDuration_time("4-5 horas");
                destino9.setDescription("Complejo arqueológico maya y una de las Nuevas Siete Maravillas del Mundo. La pirámide de Kukulcán es un testimonio de la avanzada astronomía y arquitectura maya.");
                destino9.setCategory(Category.MEXICO);
                destino9.setAddress("Yucatán, México");
                destino9.setPunctuation(4.6f);
                destino9.setCity("Chichen Itzá");
                destino9.setImageUrl("https://images.unsplash.com/photo-1518638150340-f706e86654de?w=800&h=600&fit=crop");
                destino9.setSecondaryImages(Arrays.asList(
                    "https://images.unsplash.com/photo-1512813195386-6cf811ad3542?w=400&h=300&fit=crop",
                    "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400&h=300&fit=crop"
                ));
                destino9.setLanguages(new HashSet<>(Arrays.asList(Language.SPANISH, Language.ENGLISH)));
                destinoRepository.save(destino9);
            }

            // Destino 10: Cancún - México
            if (!destinoRepository.existsByNameIgnoreCase("Cancún")) {
                Destino destino10 = new Destino();
                destino10.setName("Cancún");
                destino10.setPrecio(60.0f);
                destino10.setDuration_time("3-7 días");
                destino10.setDescription("Destino turístico de clase mundial en la Riviera Maya. Playas de arena blanca, aguas turquesas del Caribe, vida nocturna vibrante y acceso a sitios arqueológicos mayas.");
                destino10.setCategory(Category.MEXICO);
                destino10.setAddress("Cancún, Quintana Roo, México");
                destino10.setPunctuation(4.5f);
                destino10.setCity("Cancún");
                destino10.setImageUrl("https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=800&h=600&fit=crop");
                destino10.setSecondaryImages(Arrays.asList(
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=300&fit=crop",
                    "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop"
                ));
                destino10.setLanguages(new HashSet<>(Arrays.asList(Language.SPANISH, Language.ENGLISH)));
                destinoRepository.save(destino10);
            }

            System.out.println("✅ 10 sample destinations created successfully!");
        } else {
            System.out.println("Destinations already exist in database, skipping creation.");
        }
    }
}
