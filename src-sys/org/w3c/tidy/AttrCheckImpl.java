// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2007-3-20 下午 04:57:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) ansi 
// Source File Name:   AttrCheckImpl.java

package org.w3c.tidy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// Referenced classes of package org.w3c.tidy:
//            AttrCheck, AttVal, Lexer, Report, 
//            Node, TidyUtils, Configuration, TagTable, 
//            Dict

public final class AttrCheckImpl implements Serializable
{
    public static class CheckLang
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if("lang".equals(attval.attribute))
                lexer.constrainVersion(-1025);
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            } else
            {
                return;
            }
        }

        public CheckLang()
        {
        }
    }

    public static class CheckTextDir
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            attval.checkLowerCaseAttrValue(lexer, node);
            if(!TidyUtils.isInValuesIgnoreCase(VALID_VALUES, attval.value))
                lexer.report.attrError(lexer, node, attval, (short)51);
        }

        private static final String VALID_VALUES[] = {
            "rtl", "ltr"
        };


        public CheckTextDir()
        {
        }
    }

    public static class CheckScroll
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            attval.checkLowerCaseAttrValue(lexer, node);
            if(!TidyUtils.isInValuesIgnoreCase(VALID_VALUES, attval.value))
                lexer.report.attrError(lexer, node, attval, (short)51);
        }

        private static final String VALID_VALUES[] = {
            "no", "yes", "auto"
        };


        public CheckScroll()
        {
        }
    }

    public static class CheckVType
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            attval.checkLowerCaseAttrValue(lexer, node);
            if(!TidyUtils.isInValuesIgnoreCase(VALID_VALUES, attval.value))
                lexer.report.attrError(lexer, node, attval, (short)51);
        }

        private static final String VALID_VALUES[] = {
            "data", "object", "ref"
        };


        public CheckVType()
        {
        }
    }

    public static class CheckColor
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            boolean hexUppercase;
            boolean invalid;
            boolean found;
            String given;
label0:
            {
                hexUppercase = true;
                invalid = false;
                found = false;
                if(attval.value == null || attval.value.length() == 0)
                {
                    lexer.report.attrError(lexer, node, attval, (short)50);
                    return;
                }
                given = attval.value;
                Iterator colorIter = COLORS.entrySet().iterator();
                do
                {
                    java.util.Map.Entry color;
                    do
                    {
                        if(!colorIter.hasNext())
                            break label0;
                        color = (java.util.Map.Entry)colorIter.next();
                        if(given.charAt(0) != '#')
                            break;
                        if(given.length() != 7)
                        {
                            lexer.report.attrError(lexer, node, attval, (short)51);
                            invalid = true;
                            break label0;
                        }
                        if(given.equalsIgnoreCase((String)color.getValue()))
                        {
                            if(lexer.configuration.replaceColor)
                                attval.value = (String)color.getKey();
                            found = true;
                            break label0;
                        }
                    } while(true);
                    if(!TidyUtils.isLetter(given.charAt(0)))
                        break;
                    if(given.equalsIgnoreCase((String)color.getKey()))
                    {
                        if(lexer.configuration.replaceColor)
                            attval.value = (String)color.getKey();
                        found = true;
                        break label0;
                    }
                } while(true);
                lexer.report.attrError(lexer, node, attval, (short)51);
                invalid = true;
            }
            if(!found && !invalid)
                if(given.charAt(0) == '#')
                {
                    int i = 1;
                    do
                    {
                        if(i >= 7)
                            break;
                        if(!TidyUtils.isDigit(given.charAt(i)) && "abcdef".indexOf(Character.toLowerCase(given.charAt(i))) == -1)
                        {
                            lexer.report.attrError(lexer, node, attval, (short)51);
                            invalid = true;
                            break;
                        }
                        i++;
                    } while(true);
                    if(!invalid && hexUppercase)
                        for(i = 1; i < 7; i++)
                            attval.value = given.toUpperCase();

                } else
                {
                    lexer.report.attrError(lexer, node, attval, (short)51);
                    invalid = true;
                }
        }

        private static final Map COLORS;

        static 
        {
            COLORS = new HashMap();
            COLORS.put("black", "#000000");
            COLORS.put("green", "#008000");
            COLORS.put("silver", "#C0C0C0");
            COLORS.put("lime", "#00FF00");
            COLORS.put("gray", "#808080");
            COLORS.put("olive", "#808000");
            COLORS.put("white", "#FFFFFF");
            COLORS.put("yellow", "#FFFF00");
            COLORS.put("maroon", "#800000");
            COLORS.put("navy", "#000080");
            COLORS.put("red", "#FF0000");
            COLORS.put("blue", "#0000FF");
            COLORS.put("purple", "#800080");
            COLORS.put("teal", "#008080");
            COLORS.put("fuchsia", "#FF00FF");
            COLORS.put("aqua", "#00FFFF");
        }

        public CheckColor()
        {
        }
    }

    public static class CheckName
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            if(lexer.configuration.tt.isAnchorElement(node))
            {
                lexer.constrainVersion(-1025);
                Node old;
                if((old = lexer.configuration.tt.getNodeByAnchor(attval.value)) != null && old != node)
                    lexer.report.attrError(lexer, node, attval, (short)66);
                else
                    lexer.configuration.tt.anchorList = lexer.configuration.tt.addAnchor(attval.value, node);
            }
        }

        public CheckName()
        {
        }
    }

    public static class CheckId
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null || attval.value.length() == 0)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            String p = attval.value;
            char s = p.charAt(0);
            if(p.length() == 0 || !Character.isLetter(p.charAt(0)))
            {
                if(lexer.isvoyager && (TidyUtils.isXMLLetter(s) || s == '_' || s == ':'))
                    lexer.report.attrError(lexer, node, attval, (short)71);
                else
                    lexer.report.attrError(lexer, node, attval, (short)51);
            } else
            {
                int j = 1;
                do
                {
                    if(j >= p.length())
                        break;
                    s = p.charAt(j);
                    if(!TidyUtils.isNamechar(s))
                    {
                        if(lexer.isvoyager && TidyUtils.isXMLNamechar(s))
                            lexer.report.attrError(lexer, node, attval, (short)71);
                        else
                            lexer.report.attrError(lexer, node, attval, (short)51);
                        break;
                    }
                    j++;
                } while(true);
            }
            Node old;
            if((old = lexer.configuration.tt.getNodeByAnchor(attval.value)) != null && old != node)
                lexer.report.attrError(lexer, node, attval, (short)66);
            else
                lexer.configuration.tt.anchorList = lexer.configuration.tt.addAnchor(attval.value, node);
        }

        public CheckId()
        {
        }
    }

    public static class CheckNumber
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            if(("cols".equalsIgnoreCase(attval.attribute) || "rows".equalsIgnoreCase(attval.attribute)) && node.tag == lexer.configuration.tt.tagFrameset)
                return;
            String value = attval.value;
            int j = 0;
            if(node.tag == lexer.configuration.tt.tagFont && (value.startsWith("+") || value.startsWith("-")))
                j++;
            do
            {
                if(j >= value.length())
                    break;
                char p = value.charAt(j);
                if(!Character.isDigit(p))
                {
                    lexer.report.attrError(lexer, node, attval, (short)51);
                    break;
                }
                j++;
            } while(true);
        }

        public CheckNumber()
        {
        }
    }

    public static class CheckScope
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            attval.checkLowerCaseAttrValue(lexer, node);
            if(!TidyUtils.isInValuesIgnoreCase(VALID_VALUES, attval.value))
                lexer.report.attrError(lexer, node, attval, (short)51);
        }

        private static final String VALID_VALUES[] = {
            "row", "rowgroup", "col", "colgroup"
        };


        public CheckScope()
        {
        }
    }

    public static class CheckShape
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            attval.checkLowerCaseAttrValue(lexer, node);
            if(!TidyUtils.isInValuesIgnoreCase(VALID_VALUES, attval.value))
                lexer.report.attrError(lexer, node, attval, (short)51);
        }

        private static final String VALID_VALUES[] = {
            "rect", "default", "circle", "poly"
        };


        public CheckShape()
        {
        }
    }

    public static class CheckClear
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                attval.value = VALID_VALUES[0];
                return;
            }
            attval.checkLowerCaseAttrValue(lexer, node);
            if(!TidyUtils.isInValuesIgnoreCase(VALID_VALUES, attval.value))
                lexer.report.attrError(lexer, node, attval, (short)51);
        }

        private static final String VALID_VALUES[] = {
            "none", "left", "right", "all"
        };


        public CheckClear()
        {
        }
    }

    public static class CheckFsubmit
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            attval.checkLowerCaseAttrValue(lexer, node);
            if(!TidyUtils.isInValuesIgnoreCase(VALID_VALUES, attval.value))
                lexer.report.attrError(lexer, node, attval, (short)51);
        }

        private static final String VALID_VALUES[] = {
            "get", "post"
        };


        public CheckFsubmit()
        {
        }
    }

    public static class CheckTarget
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            lexer.constrainVersion(-5);
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            String value = attval.value;
            //Added by biaoping.yin on 2007-03-20 start.
            if(value.length() <= 0) 
            	return;
            //Added by biaoping.yin on 2007-03-20 end.
            if(Character.isLetter(value.charAt(0)))
                return;
            if(!TidyUtils.isInValuesIgnoreCase(VALID_VALUES, value))
                lexer.report.attrError(lexer, node, attval, (short)51);
        }

        private static final String VALID_VALUES[] = {
            "_blank", "_self", "_parent", "_top"
        };


        public CheckTarget()
        {
        }
    }

    public static class CheckLength
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            if("width".equalsIgnoreCase(attval.attribute) && (node.tag == lexer.configuration.tt.tagCol || node.tag == lexer.configuration.tt.tagColgroup))
                return;
            String p = attval.value;
            
            if(p.length() == 0 || !Character.isDigit(p.charAt(0)) && '%' != p.charAt(0))
            {
                lexer.report.attrError(lexer, node, attval, (short)51);
            } else
            {
                TagTable tt = lexer.configuration.tt;
                int j = 1;
                do
                {
                    if(j >= p.length())
                        break;
                    if(!Character.isDigit(p.charAt(j)) && (node.tag == tt.tagTd || node.tag == tt.tagTh) || !Character.isDigit(p.charAt(j)) && p.charAt(j) != '%')
                    {
                        lexer.report.attrError(lexer, node, attval, (short)51);
                        break;
                    }
                    j++;
                } while(true);
            }
        }

        public CheckLength()
        {
        }
    }

    public static class CheckBool
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                return;
            } else
            {
                attval.checkLowerCaseAttrValue(lexer, node);
                return;
            }
        }

        public CheckBool()
        {
        }
    }

    public static class CheckValign
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            attval.checkLowerCaseAttrValue(lexer, node);
            String value = attval.value;
            if(TidyUtils.isInValuesIgnoreCase(VALID_VALUES, value))
                return;
            if(TidyUtils.isInValuesIgnoreCase(VALID_VALUES_IMG, value))
            {
                if(node.tag == null || (node.tag.model & 0x10000) == 0)
                    lexer.report.attrError(lexer, node, attval, (short)51);
            } else
            if(TidyUtils.isInValuesIgnoreCase(VALID_VALUES_PROPRIETARY, value))
            {
                lexer.constrainVersion(448);
                lexer.report.attrError(lexer, node, attval, (short)54);
            } else
            {
                lexer.report.attrError(lexer, node, attval, (short)51);
            }
        }

        private static final String VALID_VALUES[] = {
            "top", "middle", "bottom", "baseline"
        };
        private static final String VALID_VALUES_IMG[] = {
            "left", "right"
        };
        private static final String VALID_VALUES_PROPRIETARY[] = {
            "texttop", "absmiddle", "absbottom", "textbottom"
        };


        public CheckValign()
        {
        }
    }

    public static class CheckAlign
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            if(node.tag != null && (node.tag.model & 0x10000) != 0)
            {
                AttrCheckImpl.VALIGN.check(lexer, node, attval);
                return;
            }
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            attval.checkLowerCaseAttrValue(lexer, node);
            if(!TidyUtils.isInValuesIgnoreCase(VALID_VALUES, attval.value))
                lexer.report.attrError(lexer, node, attval, (short)51);
        }

        private static final String VALID_VALUES[] = {
            "left", "center", "right", "justify"
        };


        public CheckAlign()
        {
        }
    }

    public static class CheckScript
        implements AttrCheck
    {

        public void check(Lexer lexer1, Node node1, AttVal attval1)
        {
        }

        public CheckScript()
        {
        }
    }

    public static class CheckUrl
        implements AttrCheck
    {

        public void check(Lexer lexer, Node node, AttVal attval)
        {
            boolean escapeFound = false;
            boolean backslashFound = false;
            int i = 0;
            if(attval.value == null)
            {
                lexer.report.attrError(lexer, node, attval, (short)50);
                return;
            }
            String p = attval.value;
            for(i = 0; i < p.length(); i++)
            {
                char c = p.charAt(i);
                if(c == '\\')
                {
                    backslashFound = true;
                    continue;
                }
                if(c > '~' || c <= ' ' || c == '<' || c == '>')
                    escapeFound = true;
            }

            if(lexer.configuration.fixBackslash && backslashFound)
            {
                attval.value = attval.value.replace('\\', '/');
                p = attval.value;
            }
            if(lexer.configuration.fixUri && escapeFound)
            {
                StringBuffer dest = new StringBuffer();
                for(i = 0; i < p.length(); i++)
                {
                    char c = p.charAt(i);
                    if(c > '~' || c <= ' ' || c == '<' || c == '>')
                    {
                        dest.append('%');
                        dest.append(Integer.toHexString(c).toUpperCase());
                    } else
                    {
                        dest.append(c);
                    }
                }

                attval.value = dest.toString();
            }
            if(backslashFound)
                if(lexer.configuration.fixBackslash)
                    lexer.report.attrError(lexer, node, attval, (short)62);
                else
                    lexer.report.attrError(lexer, node, attval, (short)61);
            if(escapeFound)
            {
                if(lexer.configuration.fixUri)
                    lexer.report.attrError(lexer, node, attval, (short)64);
                else
                    lexer.report.attrError(lexer, node, attval, (short)63);
                lexer.badChars |= 0x51;
            }
        }

        public CheckUrl()
        {
        }
    }


    private AttrCheckImpl()
    {
    }

    public static final AttrCheck URL = new CheckUrl();
    public static final AttrCheck SCRIPT = new CheckScript();
    public static final AttrCheck NAME = new CheckName();
    public static final AttrCheck ID = new CheckId();
    public static final AttrCheck ALIGN = new CheckAlign();
    public static final AttrCheck VALIGN = new CheckValign();
    public static final AttrCheck BOOL = new CheckBool();
    public static final AttrCheck LENGTH = new CheckLength();
    public static final AttrCheck TARGET = new CheckTarget();
    public static final AttrCheck FSUBMIT = new CheckFsubmit();
    public static final AttrCheck CLEAR = new CheckClear();
    public static final AttrCheck SHAPE = new CheckShape();
    public static final AttrCheck NUMBER = new CheckNumber();
    public static final AttrCheck SCOPE = new CheckScope();
    public static final AttrCheck COLOR = new CheckColor();
    public static final AttrCheck VTYPE = new CheckVType();
    public static final AttrCheck SCROLL = new CheckScroll();
    public static final AttrCheck TEXTDIR = new CheckTextDir();
    public static final AttrCheck LANG = new CheckLang();
    public static final AttrCheck TEXT = null;
    public static final AttrCheck CHARSET = null;
    public static final AttrCheck TYPE = null;
    public static final AttrCheck CHARACTER = null;
    public static final AttrCheck URLS = null;
    public static final AttrCheck COLS = null;
    public static final AttrCheck COORDS = null;
    public static final AttrCheck DATE = null;
    public static final AttrCheck IDREF = null;
    public static final AttrCheck TFRAME = null;
    public static final AttrCheck FBORDER = null;
    public static final AttrCheck MEDIA = null;
    public static final AttrCheck LINKTYPES = null;
    public static final AttrCheck TRULES = null;

}