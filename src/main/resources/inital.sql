
###########请先创建数据库##################
#create database ocp default charset utf8;
#use ocp;
##########################################
########################3.9
create table if not exists `opc_operation_user`
(
    `id`       int(11)     NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password`     varchar(50) NOT NULL COMMENT '用户密码，MD5加密',
    `realname` varchar(50) NOT NULL  COMMENT '真实姓名',
    `status` tinyint(1) DEFAULT '1' NOT NULL COMMENT '状态 1-启用 0-禁用',
    `institution_id` int(11) NOT NULL COMMENT '机构id',
    `phone` varchar(50) COMMENT '电话',
    `email` varchar(50) COMMENT '邮箱',
    `sex` tinyint(1) NOT NULL default '1' COMMENT '1-男 0-女',
    `create_time`  datetime    NOT NULL COMMENT '创建时间',
    `update_time`  datetime    NOT NULL COMMENT '最后一次更新时间',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8 ;

create table if not exists `opc_institution`(
    `id` int(11)     NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `uid` int(11) NOT NULL COMMENT '机构的编号',
    `name` varchar(50) NOT NULL COMMENT '机构名称',
    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1-open,0-close',
    `parent_id` int(11) NOT NULL COMMENT  '父机构的id号，为0代表是最父类机构',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8 ;

create table if not exists `opc_role`(
    `id` int(11)     NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `name` varchar(50) NOT NULL COMMENT '角色名',
    `describe` varchar(50)  COMMENT  '角色描述',
    `status` tinyint(1) DEFAULT '1' NOT NULL COMMENT '状态 1-启用 0-禁用',
    `create_time`  datetime    NOT NULL COMMENT '创建时间',
    `update_time`  datetime    NOT NULL COMMENT '最后一次更新时间',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8;

create table if not exists `user_role`(
    `id` int(11)     NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `role_id` int(50) NOT NULL COMMENT '角色id',
    `uid` int(11) NOT NULL COMMENT '运营账户的id',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8;

create table if not exists `operation_role_authority`(
    `id` int(11)     NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `authority_id` int(11) NOT NULL COMMENT '权限id',
    `role_id` int(11) NOT NULL COMMENT '该权限属于哪个角色',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8;


create table if not exists `operation_authority`(
         `id` int(11)     NOT NULL AUTO_INCREMENT COMMENT 'Id',
         `authority_name` varchar(50) NOT NULL  COMMENT '权限名字',
         `uri` varchar(50) NOT NULL COMMENT '路由',
         PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8;


create table if not exists `operation_user`(
   `id` int(11)     NOT NULL AUTO_INCREMENT COMMENT 'Id',
   `username` varchar(50) NOT NULL COMMENT '用户名',
   `password` VARCHAR(50) NOT NULL COMMENT '密码',
   `role_id` int(11) NOT NULL COMMENT '角色id',
   `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1-open,0-close',
   `category_id` int(11) NOT NULL COMMENT '0-运营账号，1-经销商账号 2- 经销商子账号',
   PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8;
############3.10

create table if not exists `opc_dealer_info`( /*经销商信息*/
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
    `name` varchar(50) NOT NULL COMMENT '经销商名称',
    `short_name` varchar(50) NOT NULL COMMENT '经销商简称',
    `uuid` varchar(50) NOT NULL COMMENT '经销商编码',
    `institution_id` int(11) NOT NULL COMMENT '机构id',
    `category_id` int(11) NOT NULL COMMENT '经销商类别id',
    `note` varchar(50) NOT NULL COMMENT '经销商备注',
    `manager_id` int(11) NOT NULL DEFAULT '0' COMMENT '经销商管理者的id,默认为0视为没有被管理者关联',
    `status` tinyint(1) DEFAULT '1' NOT NULL COMMENT '状态 1-启用 0-禁用',
    `create_time`  datetime    NOT NULL COMMENT '创建时间',
    `update_time`  datetime    NOT NULL COMMENT '最后一次更新时间',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8;

create table if not exists `dealer_category`(
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name` varchar(50) NOT NULL COMMENT '分类名称',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8;

create table if not exists `opc_depot_info`(/*收货地址*/
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `name` varchar(50) NOT NULL COMMENT '联系人',
  `phone` varchar(50) NOT NULL COMMENT '联系电话',
  `address` varchar(50) NOT NULL COMMENT '收货地址',
  `addressInstitution` varchar(50) COMMENT '收货单位',
  `note` varchar(50) COMMENT '备注',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1-yes,0-no',
  `uuid` varchar(50)  COMMENT '身份证',
  `consumer_id` int(11) NOT NULL COMMENT '关联的经销商账户',
  PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8;

create table if not exists `opc_dealer_child`
(
    `id`       int(11)     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `realname` varchar(50) NOT NULL COMMENT '真实姓名',
    `role_id`  int(11)     NOT NULL COMMENT '角色id',
    `password` varchar(50) NOT NULL COMMENT '经销商子账号密码',
    `sex`      tinyint(1)  NOT NULL default '1' COMMENT '1-男 0-女',
    `phone`    varchar(50) COMMENT '手机号',
    `email`    varchar(50) COMMENT '电子邮箱',
    `note`     varchar(50) COMMENT '备注',
    `status` tinyint(1) DEFAULT '1' NOT NULL COMMENT '状态 1-启用 0-禁用',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8;

create table if not exists `opc_manage_dealer_info`(
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
     `username` varchar(50) NOT NULL COMMENT '经销商用户名',
     `name` varchar(50) NOT NULL COMMENT '经销商姓名',
     `password` varchar(50) NOT NULL COMMENT '经销商密码',
     `contact_name` varchar(50) COMMENT '经销商联系人',
     `phone` varchar(50)  COMMENT '经销商手机号',
     `email` varchar(50)  COMMENT '经销商邮箱',
     `last_modifier` varchar(50) COMMENT '最后更新人',
     `note` varchar(50)  COMMENT '经销商备注',
     `status` tinyint(1) DEFAULT '1' NOT NULL COMMENT '状态 1-启用 0-禁用',
     `create_time`  datetime    NOT NULL COMMENT '创建时间',
     `update_time`  datetime    NOT NULL COMMENT '最后一次更新时间',
     PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET=utf8;





#######3.1
create table if not exists `opc_order_info`(
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `child_id` int(11) NOT NULL COMMENT '经销商子账户id',
    `dealer_id` int(11) NOT NULL COMMENT '经销商id',
    `depot_id` int(11) NOT NULL COMMENT '仓库id',
    `order_id` varchar(50) NOT NULL COMMENT '该订单id',
    `get_product_year` int(11) NOT NULL COMMENT '备货时间年',
    `get_product_month` int(11) NOT NULL COMMENT '备货月',
    `get_product_month_detailed` int(11) NOT NULL COMMENT '0-上旬，1-中旬，2-下旬',
    `note` varchar(50) COMMENT '备注',
    `area` varchar(50) Comment '区域',
    `status` int(11) NOT NULL COMMENT '1-未提交,2-已经提交等待初核实,3-待上传附件,4-已上传附件等待复核,5-驳回等待再次上传,6-已发货,0-已取消',
    `reject_reason` varchar(50) COMMENT '驳回原因',
    `total_volume` int(11) NOT NULL COMMENT '总体积',
    `total_count` int(11) NOT NULL COMMENT '总数量',
    `total_amount` decimal(10) NOT NULL COMMENT '总金额',
    `first_verify_name` varchar(50) COMMENT '初审人名称',
    `first_verify_time` datetime COMMENT '初审时间',
    `review_verify_name` varchar(50) COMMENT '复审人名称',
    `review_verify_time` datetime COMMENT '复审时间',
    `create_time` datetime COMMENT '订单创建时间',
    `update_time` datetime COMMENT '订单更新时间',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET =utf8;

create table if not exists `order_file`(
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `file_path` varchar(50) NOT NULL COMMENT '文件地址',
    `order_id` int(11) NOT NULL COMMENT '订单id',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET =utf8;

create table if not exists `order_detail_info`(
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `product_id` int(11) NOT NULL COMMENT '产品id',
    `order_id` int(11) NOT NULL COMMENT '订单id',
    `count` int(11) NOT NULL COMMENT '该商品数量',
    `open_fare` decimal(10) NOT NULL COMMENT '直接开票价',
    `amount` decimal(10) NOT NULL COMMENT '金额',
    `volume` int(10) NOT NULL COMMENT '体积',
    `note` varchar(50) COMMENT '备注',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET =utf8;



create table if not exists `opc_product_info`(
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `product_id` int(11) NOT NULL COMMENT '产品编码',
    `product_name` varchar(50) NOT NULL COMMENT '产品名称',
    `product_model` varchar(50) NOT NULL COMMENT '产品型号',
    `product_volume` int(11) NOT NULL COMMENT '产品体积',
    `product_category` varchar(50) NOT NULL COMMENT '类目',
    `product_open_fare` decimal(20,2) NOT NULL COMMENT '开票价',
    `create_time` datetime COMMENT '商品创建时间',
    `update_time` datetime COMMENT '商品更新时间',
    `dealer_id` int(11) NOT NULL COMMENT '所属的经销商',
    PRIMARY KEY (`id`)
)engine =InnoDB DEFAULT CHARSET =utf8;



/*CREATE TABLE
    IF NOT EXISTS `t_base_pinyin` (
                                      `pin_yin_` VARCHAR (255) CHARACTER
                                          SET gbk NOT NULL,
                                      `code_` INT (11) NOT NULL,
                                      PRIMARY KEY (`code_`)
) ENGINE = INNODB DEFAULT CHARSET = latin1

INSERT INTO t_base_pinyin (pin_yin_, code_)
VALUES
("a", 20319),
("ai", 20317),
("an", 20304),
("ang", 20295),
("ao", 20292),
("ba", 20283),
("bai", 20265),
("ban", 20257),
("bang", 20242),
("bao", 20230),
("bei", 20051),
("ben", 20036),
("beng", 20032),
("bi", 20026),
("bian", 20002),
("biao", 19990),
("bie", 19986),
("bin", 19982),
("bing", 19976),
("bo", 19805),
("bu", 19784),
("ca", 19775),
("cai", 19774),
("can", 19763),
("cang", 19756),
("cao", 19751),
("ce", 19746),
("ceng", 19741),
("cha", 19739),
("chai", 19728),
("chan", 19725),
("chang", 19715),
("chao", 19540),
("che", 19531),
("chen", 19525),
("cheng", 19515),
("chi", 19500),
("chong", 19484),
("chou", 19479),
("chu", 19467),
("chuai", 19289),
("chuan", 19288),
("chuang", 19281),
("chui", 19275),
("chun", 19270),
("chuo", 19263),
("ci", 19261),
("cong", 19249),
("cou", 19243),
("cu", 19242),
("cuan", 19238),
("cui", 19235),
("cun", 19227),
("cuo", 19224),
("da", 19218),
("dai", 19212),
("dan", 19038),
("dang", 19023),
("dao", 19018),
("de", 19006),
("deng", 19003),
("di", 18996),
("dian", 18977),
("diao", 18961),
("die", 18952),
("ding", 18783),
("diu", 18774),
("dong", 18773),
("dou", 18763),
("du", 18756),
("duan", 18741),
("dui", 18735),
("dun", 18731),
("duo", 18722),
("e", 18710),
("en", 18697),
("er", 18696),
("fa", 18526),
("fan", 18518),
("fang", 18501),
("fei", 18490),
("fen", 18478),
("feng", 18463),
("fo", 18448),
("fou", 18447),
("fu", 18446),
("ga", 18239),
("gai", 18237),
("gan", 18231),
("gang", 18220),
("gao", 18211),
("ge", 18201),
("gei", 18184),
("gen", 18183),
("geng", 18181),
("gong", 18012),
("gou", 17997),
("gu", 17988),
("gua", 17970),
("guai", 17964),
("guan", 17961),
("guang", 17950),
("gui", 17947),
("gun", 17931),
("guo", 17928),
("ha", 17922),
("hai", 17759),
("han", 17752),
("hang", 17733),
("hao", 17730),
("he", 17721),
("hei", 17703),
("hen", 17701),
("heng", 17697),
("hong", 17692),
("hou", 17683),
("hu", 17676),
("hua", 17496),
("huai", 17487),
("huan", 17482),
("huang", 17468),
("hui", 17454),
("hun", 17433),
("huo", 17427),
("ji", 17417),
("jia", 17202),
("jian", 17185),
("jiang", 16983),
("jiao", 16970),
("jie", 16942),
("jin", 16915),
("jing", 16733),
("jiong", 16708),
("jiu", 16706),
("ju", 16689),
("juan", 16664),
("jue", 16657),
("jun", 16647),
("ka", 16474),
("kai", 16470),
("kan", 16465),
("kang", 16459),
("kao", 16452),
("ke", 16448),
("ken", 16433),
("keng", 16429),
("kong", 16427),
("kou", 16423),
("ku", 16419),
("kua", 16412),
("kuai", 16407),
("kuan", 16403),
("kuang", 16401),
("kui", 16393),
("kun", 16220),
("kuo", 16216),
("la", 16212),
("lai", 16205),
("lan", 16202),
("lang", 16187),
("lao", 16180),
("le", 16171),
("lei", 16169),
("leng", 16158),
("li", 16155),
("lia", 15959),
("lian", 15958),
("liang", 15944),
("liao", 15933),
("lie", 15920),
("lin", 15915),
("ling", 15903),
("liu", 15889),
("long", 15878),
("lou", 15707),
("lu", 15701),
("lv", 15681),
("luan", 15667),
("lue", 15661),
("lun", 15659),
("luo", 15652),
("ma", 15640),
("mai", 15631),
("man", 15625),
("mang", 15454),
("mao", 15448),
("me", 15436),
("mei", 15435),
("men", 15419),
("meng", 15416),
("mi", 15408),
("mian", 15394),
("miao", 15385),
("mie", 15377),
("min", 15375),
("ming", 15369),
("miu", 15363),
("mo", 15362),
("mou", 15183),
("mu", 15180),
("na", 15165),
("nai", 15158),
("nan", 15153),
("nang", 15150),
("nao", 15149),
("ne", 15144),
("nei", 15143),
("nen", 15141),
("neng", 15140),
("ni", 15139),
("nian", 15128),
("niang", 15121),
("niao", 15119),
("nie", 15117),
("nin", 15110),
("ning", 15109),
("niu", 14941),
("nong", 14937),
("nu", 14933),
("nv", 14930),
("nuan", 14929),
("nue", 14928),
("nuo", 14926),
("o", 14922),
("ou", 14921),
("pa", 14914),
("pai", 14908),
("pan", 14902),
("pang", 14894),
("pao", 14889),
("pei", 14882),
("pen", 14873),
("peng", 14871),
("pi", 14857),
("pian", 14678),
("piao", 14674),
("pie", 14670),
("pin", 14668),
("ping", 14663),
("po", 14654),
("pu", 14645),
("qi", 14630),
("qia", 14594),
("qian", 14429),
("qiang", 14407),
("qiao", 14399),
("qie", 14384),
("qin", 14379),
("qing", 14368),
("qiong", 14355),
("qiu", 14353),
("qu", 14345),
("quan", 14170),
("que", 14159),
("qun", 14151),
("ran", 14149),
("rang", 14145),
("rao", 14140),
("re", 14137),
("ren", 14135),
("reng", 14125),
("ri", 14123),
("rong", 14122),
("rou", 14112),
("ru", 14109),
("ruan", 14099),
("rui", 14097),
("run", 14094),
("ruo", 14092),
("sa", 14090),
("sai", 14087),
("san", 14083),
("sang", 13917),
("sao", 13914),
("se", 13910),
("sen", 13907),
("seng", 13906),
("sha", 13905),
("shai", 13896),
("shan", 13894),
("shang", 13878),
("shao", 13870),
("she", 13859),
("shen", 13847),
("sheng", 13831),
("shi", 13658),
("shou", 13611),
("shu", 13601),
("shua", 13406),
("shuai", 13404),
("shuan", 13400),
("shuang", 13398),
("shui", 13395),
("shun", 13391),
("shuo", 13387),
("si", 13383),
("song", 13367),
("sou", 13359),
("su", 13356),
("suan", 13343),
("sui", 13340),
("sun", 13329),
("suo", 13326),
("ta", 13318),
("tai", 13147),
("tan", 13138),
("tang", 13120),
("tao", 13107),
("te", 13096),
("teng", 13095),
("ti", 13091),
("tian", 13076),
("tiao", 13068),
("tie", 13063),
("ting", 13060),
("tong", 12888),
("tou", 12875),
("tu", 12871),
("tuan", 12860),
("tui", 12858),
("tun", 12852),
("tuo", 12849),
("wa", 12838),
("wai", 12831),
("wan", 12829),
("wang", 12812),
("wei", 12802),
("wen", 12607),
("weng", 12597),
("wo", 12594),
("wu", 12585),
("xi", 12556),
("xia", 12359),
("xian", 12346),
("xiang", 12320),
("xiao", 12300),
("xie", 12120),
("xin", 12099),
("xing", 12089),
("xiong", 12074),
("xiu", 12067),
("xu", 12058),
("xuan", 12039),
("xue", 11867),
("xun", 11861),
("ya", 11847),
("yan", 11831),
("yang", 11798),
("yao", 11781),
("ye", 11604),
("yi", 11589),
("yin", 11536),
("ying", 11358),
("yo", 11340),
("yong", 11339),
("you", 11324),
("yu", 11303),
("yuan", 11097),
("yue", 11077),
("yun", 11067),
("za", 11055),
("zai", 11052),
("zan", 11045),
("zang", 11041),
("zao", 11038),
("ze", 11024),
("zei", 11020),
("zen", 11019),
("zeng", 11018),
("zha", 11014),
("zhai", 10838),
("zhan", 10832),
("zhang", 10815),
("zhao", 10800),
("zhe", 10790),
("zhen", 10780),
("zheng", 10764),
("zhi", 10587),
("zhong", 10544),
("zhou", 10533),
("zhu", 10519),
("zhua", 10331),
("zhuai", 10329),
("zhuan", 10328),
("zhuang", 10322),
("zhui", 10315),
("zhun", 10309),
("zhuo", 10307),
("zi", 10296),
("zong", 10281),
("zou", 10274),
("zu", 10270),
("zuan", 10262),
("zui", 10260),
("zun", 10256),
("zuo", 10254);

DROP FUNCTION if EXISTS to_pinyin ;
DELIMITER $
CREATE FUNCTION to_pinyin(NAME VARCHAR(255) CHARSET gbk)
    RETURNS VARCHAR(255) CHARSET gbk
BEGIN
    DECLARE mycode INT;
    DECLARE tmp_lcode VARCHAR(2) CHARSET gbk;
    DECLARE lcode INT;
    DECLARE tmp_rcode VARCHAR(2) CHARSET gbk;
    DECLARE rcode INT;
    DECLARE mypy VARCHAR(255) CHARSET gbk DEFAULT '';
    DECLARE lp INT;
    SET mycode = 0;
    SET lp = 1;
    SET NAME = HEX(NAME);
    WHILE lp < LENGTH(NAME) DO
            SET tmp_lcode = SUBSTRING(NAME, lp, 2);
            SET lcode = CAST(ASCII(UNHEX(tmp_lcode)) AS UNSIGNED);
            SET tmp_rcode = SUBSTRING(NAME, lp + 2, 2);
            SET rcode = CAST(ASCII(UNHEX(tmp_rcode)) AS UNSIGNED);
            IF lcode > 128 THEN
                SET mycode =65536 - lcode * 256 - rcode ;
                SELECT CONCAT(mypy,pin_yin_) INTO mypy FROM t_base_pinyin WHERE CODE_ >= ABS(mycode) ORDER BY CODE_ ASC LIMIT 1;
                SET lp = lp + 4;
            ELSE
                SET mypy = CONCAT(mypy,CHAR(CAST(ASCII(UNHEX(SUBSTRING(NAME, lp, 2))) AS UNSIGNED)));
                SET lp = lp + 2;
            END IF;
        END WHILE;
    RETURN LOWER(mypy);
END

DROP FUNCTION IF EXISTS `GET_FIRST_PINYIN_CHAR`;
DELIMITER $
CREATE FUNCTION `GET_FIRST_PINYIN_CHAR`(PARAM VARCHAR(255))
    RETURNS varchar(2)CHARSET gbk
BEGIN
    DECLARE V_RETURN VARCHAR (255);
    DECLARE V_FIRST_CHAR VARCHAR (2);
    SET V_FIRST_CHAR = UPPER(LEFT(PARAM, 1));
    SET V_RETURN = V_FIRST_CHAR;
    IF LENGTH(V_FIRST_CHAR) <> CHARACTER_LENGTH(V_FIRST_CHAR) THEN
        SET V_RETURN = ELT(
                INTERVAL (
                          CONV(
                                  HEX(
                                          LEFT (CONVERT(PARAM USING gbk), 1)
                                      ),
                                  16,
                                  10
                              ),
                          0xB0A1,
                          0xB0C5,
                          0xB2C1,
                          0xB4EE,
                          0xB6EA,
                          0xB7A2,
                          0xB8C1,
                          0xB9FE,
                          0xBBF7,
                          0xBFA6,
                          0xC0AC,
                          0xC2E8,
                          0xC4C3,
                          0xC5B6,
                          0xC5BE,
                          0xC6DA,
                          0xC8BB,
                          0xC8F6,
                          0xCBFA,
                          0xCDDA,
                          0xCEF4,
                          0xD1B9,
                          0xD4D1
                    ),
                'A',
                'B',
                'C',
                'D',
                'E',
                'F',
                'G',
                'H',
                'J',
                'K',
                'L',
                'M',
                'N',
                'O',
                'P',
                'Q',
                'R',
                'S',
                'T',
                'W',
                'X',
                'Y',
                'Z'
            );
    END
        IF;
    RETURN V_RETURN;
END;
*/