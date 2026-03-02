package com.fuse.ai.server.web.common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailTemplateBuilder {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 生成验证码邮件HTML内容
     */
    public String buildVerificationEmailHtml(String verificationCode, int expiryMinutes,
                                             String userEmail) {

        Map<String, String> variables = new HashMap<>();
        variables.put("VERIFICATION_CODE", verificationCode);
        variables.put("EXPIRY_MINUTES", String.valueOf(expiryMinutes));
        variables.put("USER_EMAIL", userEmail);
        variables.put("REQUEST_TIME", LocalDateTime.now().format(TIME_FORMATTER));

        // 使用 StringBuilder 避免文本块格式化问题
        StringBuilder template = new StringBuilder();
        template.append("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Email Verification - FuseAI</title>
                <style>
                    body { font-family: -apple-system, BlinkMacSystemFont, sans-serif; line-height: 1.6; color: #333; background: #f8fafc; margin: 0; padding: 20px; }
                    .email-wrapper { max-width: 600px; margin: 0 auto; background: white; border-radius: 16px; box-shadow: 0 10px 40px rgba(0,0,0,0.08); overflow: hidden; }
                    .header { background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%); color: white; padding: 32px 40px; text-align: center; }
                    .header h1 { margin: 0; font-size: 24px; font-weight: 600; }
                    .content { padding: 40px; }
                    .code-display { background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%); border: 2px solid #0ea5e9; border-radius: 12px; padding: 32px; text-align: center; margin: 32px 0; }
                    .code { font-size: 42px; font-weight: 700; color: #0ea5e9; letter-spacing: 6px; font-family: 'Courier New', monospace; margin: 16px 0; }
                    .label { color: #64748b; font-size: 14px; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 8px; }
                    .info-box { background: #f1f5f9; border-radius: 8px; padding: 20px; margin: 24px 0; }
                    .info-item { display: flex; align-items: center; margin-bottom: 12px; color: #475569; }
                    .info-icon { margin-right: 12px; font-size: 18px; }
                    .security { background: #fef2f2; border: 1px solid #fecaca; border-radius: 8px; padding: 20px; margin: 24px 0; }
                    .security-title { color: #dc2626; font-weight: 600; margin-bottom: 12px; display: flex; align-items: center; gap: 8px; }
                    .footer { background: #f8fafc; padding: 32px 40px; text-align: center; border-top: 1px solid #e2e8f0; }
                    @media (max-width: 640px) { .content, .header, .footer { padding: 24px; } .code { font-size: 32px; letter-spacing: 4px; } }
                </style>
            </head>
            <body>
                <div class="email-wrapper">
                    <div class="header">
                        <h1>🔐 Email Verification</h1>
                    </div>
                    
                    <div class="content">
                        <p style="color: #475569; margin-bottom: 24px;">
                            Hello,<br>
                            Please use the following verification code to complete your authentication process.
                        </p>
                        
                        <div class="code-display">
                            <div class="label">Verification Code</div>
                            <div class="code">%s</div>
                            <div style="color: #64748b; font-size: 14px; margin-top: 8px;">
                                Enter this code on the verification page
                            </div>
                        </div>
                        
                        <div class="info-box">
                            <div class="info-item">
                                <span class="info-icon">⏱️</span>
                                <span><strong>Valid for:</strong> %s minutes</span>
                            </div>
                            <div class="info-item">
                                <span class="info-icon">📧</span>
                                <span><strong>Recipient:</strong> %s</span>
                            </div>
                            <div class="info-item">
                                <span class="info-icon">🕐</span>
                                <span><strong>Requested at:</strong> %s</span>
                            </div>
                        </div>
                        
                        <div class="security">
                            <div class="security-title">
                                <span>⚠️</span> Security Reminder
                            </div>
                            <ul style="color: #475569; margin: 0; padding-left: 20px;">
                                <li>Never share this code with anyone</li>
                                <li>FuseAI staff will never ask for your verification code</li>
                                <li>This code will expire in %s minutes</li>
                                <li>If you didn't request this, please ignore this email</li>
                            </ul>
                        </div>
                        
                        <!-- 已移除Complete Verification按钮 -->
                    </div>
                    
                    <div class="footer">
                        <div style="color: #64748b; font-size: 14px; margin-bottom: 16px;">
                            <strong>FuseAI Tools</strong><br>
                            AI-powered creativity platform
                        </div>
                        <div style="color: #94a3b8; font-size: 12px;">
                            <p>This is an automated message, please do not reply to this email.</p>
                            <p>Need help? Contact <a href="mailto:support@fuseaitools.com" style="color: #6366f1;">support@fuseaitools.com</a></p>
                            <p style="margin-top: 20px; padding-top: 16px; border-top: 1px solid #e2e8f0;">
                                © 2026 Fuse Digital Tech Limited.
                            </p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """);

        // 使用替换方法而不是 String.format
        String html = template.toString();
        html = html.replaceFirst("%s", escapeHtml(verificationCode))
                .replaceFirst("%s", String.valueOf(expiryMinutes))
                .replaceFirst("%s", escapeHtml(userEmail))
                .replaceFirst("%s", variables.get("REQUEST_TIME"))
                .replaceFirst("%s", String.valueOf(expiryMinutes));

        return html;
    }

    /**
     * HTML转义，防止XSS攻击
     */
    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    /**
     * 生成纯文本备用内容
     */
    public String buildVerificationEmailText(String verificationCode, int expiryMinutes) {
        return String.format(
                "Email Verification - FuseAI%n%n" +
                        "Your verification code is: %s%n%n" +
                        "Valid for: %d minutes%n%n" +
                        "Please do not share this code with anyone.%n%n" +
                        "If you did not request this, please ignore this email.%n%n" +
                        "---%n" +
                        "FuseAI Tools%n" +
                        "https://www.fuseaitools.com%n",
                verificationCode, expiryMinutes
        );
    }
}