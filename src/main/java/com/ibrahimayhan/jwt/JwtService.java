package com.ibrahimayhan.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component // bean oluşturmak için gerekli ,bazılarına bunu eklemiyoruz çünkü onların beanı zaten oluşuyordu farklı anoasyonlarla mesela service anot. ekleyince zaten oluşuyor bean veya restcontroller ekleyince
public class JwtService {
	
	
	public static final String SECRET_KEY="O0b3MZ51xhOeMvM2kl57s7LNCzQp3uI3yb6hV4DQey0=";//h256 oluşturucuya jwtoreniyorum diye bir anahtar oluşturdum SharedSecret kısmını buraya aldım 
	
	
	public Key getKey() {//bu metot Token yaratırken ve export ederken(getclaims) kullanılacak ,bize secret keyimizi decode hale çevirip hmacshakey algoritmasına yollar bu da bize bir anahtar döner
		byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
		
		return Keys.hmacShaKeyFor(keyBytes);	
	}
	
	
	public String generateToken(UserDetails userDetails) {
		
		Map<String, Object> claimsMap = new HashMap<>();//bu map şart değil aşağıda eklemek için örnek olarak yaptım 
		claimsMap.put("role", "admin");
		
		
		return Jwts.builder()
				 .setSubject(userDetails.getUsername())//token a username ekliyorum
				 .addClaims(claimsMap)//şart değil örnek olsun diye ekledik//setclaims değil add claims olacak
				 .setIssuedAt(new Date())//tokenun oluşturulduğu tarih
				 .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*2))//tokenun biteceği tarihtir,o anki sistem saatine 2 saat ekledim(milisaniye türünden 2 saat)
				 .signWith(getKey(),SignatureAlgorithm.HS256)
				 .compact();
	}
	
	
	
	public Claims getclaims(String token) {
		Claims claims=Jwts
				.parserBuilder()//JWT çözümleme nesnesini başlatır
				.setSigningKey(getKey())//Token’ın imzasını doğrulamak için key ayarlanır
				.build()
				.parseClaimsJws(token).getBody();//Token parse edilir getbody ile de Token içeriği (claim'ler) alınır
		return claims;
	}
	
	
	
	public <T> T exportToken(String token,Function<Claims, T> claimsFunction) {
		
		Claims claims= getclaims(token);//bi üstteki metottan claimsi aldık 
		return claimsFunction.apply(claims);//claimsi oluşturup ihtiyacıma göre doldurdum şimdi aply diyerek bu doldurduğum nesnemi yollayıp karşılığında da stediğim alanı (örneğin subject, expiration) dönecek bana
		
		
	}
	
	
	public Object getClaimsByKey(String token ,String key) {//bu metodu claimsleri çekmek için yzdık token ve keyi vericez biz istediğimiz herhangi bir claimsi dönecek
		Claims claims=getclaims(token);
		return claims.get(key);
		//Diyelim ki  frontend veya uyg. herhangi yerinde role ihtiyacımız oldu admin mi veya başka kullanıcı mı öğrenmek istiyoruz  
		//tokenumuz var ve role gerekiyor key="role yaparım tokenum da varsa String role=getClaimsByKey(token,key) der rolü alırım 
		//sadece rol için değil claimsa eklediğim her şeyi key vererek alabilirim
	}
	
	public String getUsernameByToken(String token) {
		
		return exportToken(token, Claims::getSubject);//yukarıda örnek olsun diye eklediğimiz mapin çözümünü ilerde görcez
		
	}
	
	public boolean isTokenExpired(String token) {
		
		Date expiredDate=exportToken(token, Claims::getExpiration);
		
		return new Date().before(expiredDate);//anlık tarih expireddateden önceyse (yani küçükse) demekki tokenun süresi bitmemiş true döner 
	}										  //süre bittiyse zaten false döner else yazmaya gerek yok
	
	
	
	

}
