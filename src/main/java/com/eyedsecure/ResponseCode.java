/* Copyright (c) 2011, Linus Widströmer.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

* Redistributions of source code must retain the above copyright
  notice, this list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright
  notice, this list of conditions and the following
  disclaimer in the documentation and/or other materials provided
  with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.

Written by Linus Widströmer <linus.widstromer@it.su.se>, January 2011.
*/

package com.eyedsecure;

public enum ResponseCode {
 SUCCESS,                			// The request was successful
 INVALID_USERNAME_OR_PASSWORD,      // Username or password is invalid
 INVALID_PIN,      					// The pin is invalid
 FILE_MISSING,      				// File is missing
 BAD_SIGNATURE,          			// The signature verification failed.
 MISSING_PARAMETER,      			// The request lacks a parameter.
 INVALID_PARAMETER,      			// Request has an invalid parameter
 SERVER_ERROR;           			// Server Error

 /**
  * Is the response code considered an error
  *
  * @return boolean
  */
 public static boolean isErrorCode(ResponseCode code) {
     return (
             ResponseCode.INVALID_USERNAME_OR_PASSWORD.equals(code) ||
             ResponseCode.INVALID_PIN.equals(code) ||
             ResponseCode.FILE_MISSING.equals(code) ||
             ResponseCode.BAD_SIGNATURE.equals(code) ||
             ResponseCode.MISSING_PARAMETER.equals(code) ||
             ResponseCode.INVALID_PARAMETER.equals(code) ||
             ResponseCode.SERVER_ERROR.equals(code) 
     );
 }


}

