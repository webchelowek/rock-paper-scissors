package com.company;

import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Scanner;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        if(args.length == 1 || args.length%2==0){
            System.exit(0);
        }

        Key key;
        SecureRandom rand = new SecureRandom();
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128, rand);
        key = generator.generateKey();
        String key16 = new BigInteger(1, key.getEncoded()).toString(16);

        int compMove = (int) Math.round(Math.random() * (args.length-1));

        System.out.println("HMAC: " + HMAC.hmacDigest(Integer.toString(compMove), key16, "HmacSHA1").toUpperCase());

        int userMove = Main.showMenu(args);

        if (userMove == 0){
            System.exit(0);
        }

        System.out.println ("You move:" + args[userMove-1]);
        userMove--;

        if(userMove<compMove){
            result(userMove,compMove,args,"win","lose");
        }
        else if(userMove>compMove){
            result(compMove,userMove,args,"lose","win");
        }
        else{System.out.println("Draw!");}


        System.out.println("HMAC key: "+" "+key16.toUpperCase() +'\t'+ "computer move: "+args[compMove]);
    }

    public static void result(int move1,int move2,String[] args, String win, String lose){
            int distance = move2-move1;
            double length = args.length;
            double len = length/2;
            if(distance < len){
                System.out.println("you " + lose + "!");
            }
            if(distance>len){
                System.out.println("you " + win + "!");
            }
   }

    public static int showMenu(String[] args){
        Scanner scan = new Scanner(System.in);
        System.out.println("Available moves:");
        for(int i = 0; i< args.length;i++){
            int q = i+1;
            System.out.println(q + " - " + args[i]);
        }
        System.out.println("0 - exit");
        System.out.print("Enter your move:");

        String move = scan.nextLine();

        try {
           int userMove = Integer.parseInt(move);
           if (userMove > args.length || userMove<0){
               return Main.showMenu(args);
           }
           return userMove;
        } catch (NumberFormatException e) {
            System.err.println("Input a valid number");
            return Main.showMenu(args);
        }
    }
}

class HMAC {

    public static String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (byte aByte : bytes) {
                String hex = Integer.toHexString(0xFF & aByte);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return digest;
    }
}
