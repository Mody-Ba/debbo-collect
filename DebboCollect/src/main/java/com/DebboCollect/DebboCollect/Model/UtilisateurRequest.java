package com.DebboCollect.DebboCollect.Model;

import com.DebboCollect.DebboCollect.entity.Role;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurRequest {

    private String nom;

    private String email;

    private String motDePasse;

    private Role role;

}
/* role de cette classe quand swagger envoie {
  "nom": "Mody",
  "email": "mody@gmail.com",
  "motDePasse": "1234",
  "role": "ADMIN"
}Spring reçoit ça dans  UtilisateurRequest*/
