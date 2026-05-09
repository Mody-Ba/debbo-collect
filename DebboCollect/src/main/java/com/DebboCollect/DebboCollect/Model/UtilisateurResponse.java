package com.DebboCollect.DebboCollect.Model;

import com.DebboCollect.DebboCollect.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurResponse {

    private Long id;

    private String nom;

    private String email;

    private Role role;
}
/*Rôle Quand Swagger fait :
GET /users API renvoie :

{
  "id": 1,
  "nom": "Mody",
  "email": "mody@gmail.com",
  "role": "ADMIN"
}*/
