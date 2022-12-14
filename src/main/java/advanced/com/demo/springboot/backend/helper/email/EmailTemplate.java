package advanced.com.demo.springboot.backend.helper.email;

public class EmailTemplate {

    public static String buildResetPasswordMail(String name, String link) {
        return "" +
                "<html lang=\"en-US\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>\n" +
                "    <title>Reset Password Email Template</title>\n" +
                "    <meta name=\"description\" content=\"Reset Password Email Template.\">\n" +
                "    <style type=\"text/css\">\n" +
                "        a:hover {\n" +
                "            text-decoration: underline !important;\n" +
                "        }\n" +
                "\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body marginheight=\"0\" topmargin=\"0\" marginwidth=\"0\" style=\"margin: 0px; background-color: #f2f3f8;\" leftmargin=\"0\">\n" +
                "<!--100% body table-->\n" +
                "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#f2f3f8\"\n" +
                "       style=\"@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;\">\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <table style=\"background-color: #f2f3f8; max-width:670px;  margin:0 auto;\" width=\"100%\" border=\"0\"\n" +
                "                   align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                <tr>\n" +
                "                    <td style=\"height:80px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"text-align:center;\">\n" +
                "                        <a href=\"https://rakeshmandal.com\" title=\"logo\" target=\"_blank\">\n" +
                "                            <img width=\"100\" height=\"60\"\n" +
                "                                 src=\"https://download.logo.wine/logo/Spring_Framework/Spring_Framework-Logo.wine.png\"\n" +
                "                                 title=\"logo\"\n" +
                "                                 alt=\"logo\">\n" +
                "                        </a>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"height:20px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <table width=\"95%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "                               style=\"max-width:670px;background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);\">\n" +
                "                            <tr>\n" +
                "                                <td style=\"height:40px;\">&nbsp;</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td style=\"padding:0 35px;\">\n" +
                "                                    <h1 style=\"color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;\">\n" +
                "                                        Hello, " + name + "</h1>\n" +
                "                                    <h1 style=\"color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;\">\n" +
                "                                        You have\n" +
                "                                        requested to reset your password</h1>\n" +
                "                                    <span\n" +
                "                                            style=\"display:inline-block; vertical-align:middle; margin:29px 0 26px; border-bottom:1px solid #cecece; width:100px;\"></span>\n" +
                "                                    <p style=\"color:#455056; font-size:15px;line-height:24px; margin:0;\">\n" +
                "                                        We cannot simply send you your old password. A unique link to reset your\n" +
                "                                        password has been generated for you. To reset your password, click the\n" +
                "                                        following link and follow the instructions.\n" +
                "                                    </p>\n" + "\n" +
                "\n" +
                "                                    <a href=\"" + link + "\"\n" +
                "                                       style=\"background:#20e277;text-decoration:none !important; font-weight:500; margin-top:35px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 24px;display:inline-block;border-radius:50px;\">Reset\n" +
                "                                        Password</a>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td style=\"height:40px;\">&nbsp;</td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                <tr>\n" +
                "                    <td style=\"height:20px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"text-align:center;\">\n" +
                "                        <p style=\"font-size:14px; color:rgba(69, 80, 86, 0.7411764705882353); line-height:18px; margin:0 0 0;\">\n" +
                "                            &copy; <strong>RDMS</strong></p>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"height:80px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<!--/100% body table-->\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>";
    }

    public static String buildRegisterMail(String name, String link) {
        return "" +
                "<html lang=\"en-US\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>\n" +
                "    <title>User Verification Email Template</title>\n" +
                "    <meta name=\"description\" content=\"Reset Password Email Template.\">\n" +
                "    <style type=\"text/css\">\n" +
                "        a:hover {\n" +
                "            text-decoration: underline !important;\n" +
                "        }\n" +
                "\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body marginheight=\"0\" topmargin=\"0\" marginwidth=\"0\" style=\"margin: 0px; background-color: #f2f3f8;\" leftmargin=\"0\">\n" +
                "<!--100% body table-->\n" +
                "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#f2f3f8\"\n" +
                "       style=\"@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;\">\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <table style=\"background-color: #f2f3f8; max-width:670px;  margin:0 auto;\" width=\"100%\" border=\"0\"\n" +
                "                   align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                <tr>\n" +
                "                    <td style=\"height:80px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"text-align:center;\">\n" +
                "                        <a href=\"https://rakeshmandal.com\" title=\"logo\" target=\"_blank\">\n" +
                "                            <img width=\"100\" height=\"60\"\n" +
                "                                 src=\"https://download.logo.wine/logo/Spring_Framework/Spring_Framework-Logo.wine.png\"\n" +
                "                                 title=\"logo\"\n" +
                "                                 alt=\"logo\">\n" +
                "                        </a>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"height:20px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <table width=\"95%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "                               style=\"max-width:670px;background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);\">\n" +
                "                            <tr>\n" +
                "                                <td style=\"height:40px;\">&nbsp;</td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td style=\"padding:0 35px;\">\n" +
                "                                    <h1 style=\"color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;\">\n" +
                "                                        Hello, " + name + "</h1>\n" +
                "                                    <h1 style=\"color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;\">\n" +
                "                                        You have\n" +
                "                                        requested for user verification</h1>\n" +
                "                                    <span\n" +
                "                                            style=\"display:inline-block; vertical-align:middle; margin:29px 0 26px; border-bottom:1px solid #cecece; width:100px;\"></span>\n" +
                "                                    <p style=\"color:#455056; font-size:15px;line-height:24px; margin:0;\">\n" +
                "                                        To verify the user, click the\n" +
                "                                        following link and follow the instructions.\n" +
                "                                    </p>\n" + "\n" +
                "\n" +
                "                                    <a href=\"" + link + "\"\n" +
                "                                       style=\"background:#20e277;text-decoration:none !important; font-weight:500; margin-top:35px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 24px;display:inline-block;border-radius:50px;\">Verify\n" +
                "                                        User</a>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td style=\"height:40px;\">&nbsp;</td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                <tr>\n" +
                "                    <td style=\"height:20px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"text-align:center;\">\n" +
                "                        <p style=\"font-size:14px; color:rgba(69, 80, 86, 0.7411764705882353); line-height:18px; margin:0 0 0;\">\n" +
                "                            &copy; <strong>RDMS</strong></p>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td style=\"height:80px;\">&nbsp;</td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<!--/100% body table-->\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>";
    }
}
