package org.hulei.springboot.ark;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author hulei
 * @since 2025/7/17 22:30
 */

public class ARALoop {

    Map<String, String> all = new HashMap<>();

    List<String> LootItemSet_CaveDrop_T1_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T1_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T1_Weapons_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T2_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T2_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T2_Weapons_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T3_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T3_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T3_Weapons_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T4_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T4_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T4_Weapons_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Ice_T1_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Ice_T1_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Ice_T1_Weapons_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Ice_T2_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Ice_T2_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Ice_T2_Weapons_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Ice_T3_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Ice_T3_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Ice_T3_Weapons_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Swamp_T1_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Swamp_T1_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Swamp_T1_Weapons_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Swamp_T2_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Swamp_T2_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Swamp_T2_Weapons_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Swamp_T3_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Swamp_T3_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Swamp_T3_Weapons_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T1_Armor_ScorchedEarth = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T1_Saddles_ScorchedEarth = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T1_Weapons_ScorchedEarth = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T2_Saddles_ScorchedEarth = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T2_Weapons_ScorchedEarth = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T3_Saddles_ScorchedEarth = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T3_Weapons_ScorchedEarth = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T1_Armor = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T1_Saddles = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T1_Weapons = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T2_Armor = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T2_Saddles = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_T2_Weapons = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Underwater_T1_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Underwater_T1_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Underwater_T2_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Underwater_T2_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Underwater_T3_Armor_ASA = new ArrayList<>();
    List<String> LootItemSet_CaveDrop_Underwater_T3_Saddles_ASA = new ArrayList<>();
    List<String> LootItemSet_SupplyDrop_Ocean_ASA = new ArrayList<>();


    SupplyCrate SupplyCrate_Cave_QualityTier1;
    SupplyCrate SupplyCrate_Cave_QualityTier2;
    SupplyCrate SupplyCrate_Cave_QualityTier3;
    SupplyCrate SupplyCrate_Cave_QualityTier4;
    SupplyCrate SupplyCrate_IceCaveTier1;
    SupplyCrate SupplyCrate_IceCaveTier2;
    SupplyCrate SupplyCrate_IceCaveTier3;
    SupplyCrate SupplyCrate_SwampCaveTier1;
    SupplyCrate SupplyCrate_SwampCaveTier2;
    SupplyCrate SupplyCrate_SwampCaveTier3;
    SupplyCrate SupplyCrate_UnderwaterCaveTier1;
    SupplyCrate SupplyCrate_UnderwaterCaveTier2;
    SupplyCrate SupplyCrate_UnderwaterCaveTier3;
    SupplyCrate SupplyCrate_OceanInstant;
    SupplyCrate SupplyCreate_OceanInstant_High;
    SupplyCrate SupplyCrate_Cave_QualityTier1_ScorchedEarth;
    SupplyCrate SupplyCrate_Cave_QualityTier2_ScorchedEarth;
    SupplyCrate SupplyCrate_Cave_QualityTier3_ScorchedEarth;
    SupplyCrate SupplyCrate_Cave_QualityTier3_TheCenter;
    SupplyCrate SupplyCrate_Cave_QualityTier3_Ragnarok;
    SupplyCrate SupplyCrate_Cave_QualityTier4_Ragnarok;
    SupplyCrate SupplyCrate_Chest_Treasure_JacksonL;

    Map<SupplyCrate, String> allSupplyCrate = new LinkedHashMap<>();


    public void init() {

        all.put("PrimalItemArmor_ClothBoots", "粗布鞋子");
        all.put("PrimalItemArmor_ClothGloves", "粗布手套");
        all.put("PrimalItemArmor_ClothHelmet", "粗布帽子");
        all.put("PrimalItemArmor_ClothPants", "粗布裤子");
        all.put("PrimalItemArmor_ClothShirt", "粗布上衣");
        all.put("PrimalItemArmor_HideBoots", "兽皮鞋子");
        all.put("PrimalItemArmor_HideGloves", "兽皮手套");
        all.put("PrimalItemArmor_HideHelmet", "兽皮帽子");
        all.put("PrimalItemArmor_HidePants", "兽皮上衣");
        all.put("PrimalItemArmor_HideShirt", "粗布鞋子");
        all.put("PrimalItemArmor_WoodShield", "木制盾牌");

        all.put("PrimalItemArmor_DolphinSaddle", "鱼龙鞍");
        all.put("PrimalItemArmor_EquusSaddle", "庞马鞍");
        all.put("PrimalItemArmor_Gallimimus", "似鸡龙鞍");
        all.put("PrimalItemArmor_HyaenodonSaddle", "鬣齿兽鞍");
        all.put("PrimalItemArmor_MantaSaddle", "蝠鲼鞍");
        all.put("PrimalItemArmor_PachyrhinoSaddle", "厚鼻龙鞍");
        all.put("PrimalItemArmor_PachySaddle", "肿头龙鞍");
        all.put("PrimalItemArmor_ParaSaddle", "副栉龙鞍");
        all.put("PrimalItemArmor_PhiomiaSaddle", "渐新象鞍");
        all.put("PrimalItemArmor_RaptorSaddle", "迅猛龙鞍");
        all.put("PrimalItemArmor_SarcoSaddle", "帝鳄鞍");
        all.put("PrimalItemArmor_ScorpionSaddle", "巨蝎鞍");
        all.put("PrimalItemArmor_StegoSaddle", "剑龙鞍");
        all.put("PrimalItemArmor_TrikeSaddle", "三角龙鞍");
        all.put("PrimalItemArmor_TurtleSaddle", "淡水龟鞍");
        all.put("PrimalItemConsumable_Berry_Stimberry", "白色浆果");
        all.put("PrimalItemConsumable_CookedMeat", "熟肉");

        all.put("PrimalItemResource_Stone", "石头");
        all.put("PrimalItem_GasGrenade", "烟雾弹");
        all.put("PrimalItem_WeaponBola", "流星锤");
        all.put("PrimalItem_WeaponBow", "弓");
        all.put("PrimalItem_WeaponFlareGun", "信号枪");
        all.put("PrimalItem_WeaponPaintbrush", "涂料刷");
        all.put("PrimalItem_WeaponScissors", "剪刀");
        all.put("PrimalItem_WeaponSlingshot", "弹弓");
        all.put("PrimalItem_WeaponSpear", "长茅");
        all.put("PrimalItem_WeaponStoneClub", "木棒");
        all.put("PrimalItem_WeaponStoneHatchet", "石斧");
        all.put("PrimalItem_WeaponStonePick", "石镐");
        all.put("PrimalItem_WeaponTorch", "火把");
        all.put("PrimalItemAmmo_ArrowStone", "石箭");
        all.put("PrimalItemAmmo_ArrowTranq", "麻醉箭");
        all.put("PrimalItem_WeaponMagnifyingGlass", "放大镜");
        all.put("PrimalItem_WeaponSpyglass", "望远镜");

        all.put("PrimalItemArmor_AnkyloSaddle", "甲龙鞍");
        all.put("PrimalItemArmor_ArthroSaddle", "古马陆鞍");
        all.put("PrimalItemArmor_CarnoSaddle", "牛龙鞍");
        all.put("PrimalItemArmor_DiplodocusSaddle", "梁龙鞍");
        all.put("PrimalItemArmor_DireBearSaddle", "恐熊鞍");
        all.put("PrimalItemArmor_DoedSaddle", "星尾兽鞍");
        all.put("PrimalItemArmor_DunkleosteusSaddle", "邓氏鱼鞍");
        all.put("PrimalItemArmor_IguanodonSaddle", "禽龙鞍");
        all.put("PrimalItemArmor_MammothSaddle", "猛犸象鞍");
        all.put("PrimalItemArmor_MegatheriumSaddle", "大地懒鞍");
        all.put("PrimalItemArmor_SaberSaddle", "剑齿虎鞍");
        all.put("PrimalItemArmor_SpiderSaddle", "蜘蛛鞍");
        all.put("PrimalItemArmor_StagSaddle", "大角鹿鞍");
        all.put("PrimalItemArmor_TerrorBirdSaddle", "骇鸟鞍");
        all.put("PrimalItemConsumable_CookedMeat_Fish", "熟鱼肉");

        all.put("PrimalItem_WeaponCrossbow", "什字弩");
        all.put("PrimalItem_WeaponGrenade", "手雷");
        all.put("PrimalItem_WeaponMetalHatchet", "金属斧头");
        all.put("PrimalItem_WeaponMetalPick", "金属镐");
        all.put("PrimalItem_WeaponPike", "金属茅");
        all.put("PrimalItem_WeaponShotgun", "霰弹枪");
        all.put("PrimalItem_WeaponSickle", "金属镰刀");
        all.put("PrimalItem_WeaponSword", "剑");
        all.put("PrimalItemAmmo_SimpleBullet", "简易子弹");
        all.put("PrimalItemAmmo_SimpleShotgunBullet", "简易霰弹枪子弹");

        all.put("PrimalItemArmor_FurBoots", "毛皮鞋子");
        all.put("PrimalItemArmor_FurGloves", "毛皮手套");
        all.put("PrimalItemArmor_FurHelmet", "毛皮帽子");
        all.put("PrimalItemArmor_FurPants", "毛皮裤子");
        all.put("PrimalItemArmor_FurShirt", "毛皮上衣");
        all.put("PrimalItemArmor_GhillieBoots", "吉利鞋子");
        all.put("PrimalItemArmor_GhillieGloves", "吉利手套");
        all.put("PrimalItemArmor_GhillieHelmet", "吉利帽子");
        all.put("PrimalItemArmor_GhilliePants", "吉利裤子");
        all.put("PrimalItemArmor_GhillieShirt", "吉利上衣");

        all.put("PrimalItemArmor_AlloSaddle", "异特龙鞍");
        all.put("PrimalItemArmor_ChalicoSaddle", "砂矿兽鞍");
        all.put("PrimalItemArmor_KaprosuchusSaddle", "猪鳄鞍");
        all.put("PrimalItemArmor_MegalaniaSaddle", "古巨蜥鞍");
        all.put("PrimalItemArmor_Paracer_Saddle", "巨犀鞍");
        all.put("PrimalItemArmor_PelaSaddle", "伪齿鸟鞍");
        all.put("PrimalItemArmor_PlesiaSaddle", "蛇颈龙鞍");
        all.put("PrimalItemArmor_PteroSaddle", "无齿翼龙鞍");
        all.put("PrimalItemArmor_SauroSaddle", "雷龙鞍");
        all.put("PrimalItemArmor_ToadSaddle", "魔鬼蛙鞍");
        all.put("PrimalItemConsumable_HealSoup", "药酒");
        all.put("PrimalItemConsumable_Soup_CalienSoup", "卡琳汤(抗热汤)");
        all.put("PrimalItemConsumable_Soup_FocalChili", "焦红辣椒");
        all.put("PrimalItemConsumable_Soup_FriaCurry", "菲拉咖喱(抗冷汤)");
        all.put("PrimalItemConsumable_StaminaSoup", "能量药酒");


        all.put("PrimalItem_PoisonGrenade", "毒气手雷");
        all.put("PrimalItem_WeaponHarpoon", "鱼叉枪");
        all.put("PrimalItem_WeaponMachinedPistol", "制式手枪");
        all.put("PrimalItem_WeaponOneShotRifle", "长管步枪");
        all.put("PrimalItem_WeaponTripwireC4", "简易爆炸装置");
        all.put("PrimalItemAmmo_AdvancedBullet", "高级子弹");
        all.put("PrimalItemAmmo_BallistaArrow", "弩箭");
        all.put("PrimalItemAmmo_SimpleRifleBullet", "简易步枪子弹");

        all.put("PrimalItemArmor_MetalBoots", "防弹鞋子");
        all.put("PrimalItemArmor_MetalGloves", "防弹手套");
        all.put("PrimalItemArmor_MetalHelmet", "防弹帽子");
        all.put("PrimalItemArmor_MetalPants", "防弹裤子");
        all.put("PrimalItemArmor_MetalShirt", "防弹上衣");
        all.put("PrimalItemArmor_MetalShield", "金属盾");

        all.put("PrimalItem_Armor_Archelon_Saddle_ASA", "古巨龟鞍");
        all.put("PrimalItemArmor_CeratosaurusSaddle_ASA", "角鼻龙鞍");
        all.put("PrimalItemArmor_DeinotheriumSaddle_ASA", "恐象鞍");
        all.put("PrimalItemArmor_XiphSaddle_ASA", "剑射鱼鞍");
        all.put("PrimalItemArmor_ArgentavisSaddle", "阿根廷巨鹰鞍");
        all.put("PrimalItemArmor_BaryonyxSaddle", "重爪龙鞍");
        all.put("PrimalItemArmor_BasiloSaddle", "龙王鲸鞍");
        all.put("PrimalItemArmor_BeaverSaddle", "巨河狸鞍");
        all.put("PrimalItemArmor_DaeodonSaddle", "凶齿豨鞍");
        all.put("PrimalItemArmor_MegalodonSaddle", "巨齿鲨鞍");
        all.put("PrimalItemArmor_MegalosaurusSaddle", "斑龙鞍");
        all.put("PrimalItemArmor_ParacerSaddle_Platform", "巨犀平台鞍");
        all.put("PrimalItemArmor_RhinoSaddle", "披毛犀鞍");
        all.put("PrimalItemArmor_TapejaraSaddle", "古神翼龙鞍");
        all.put("PrimalItemArmor_ThylacoSaddle", "袋狮鞍");
        all.put("PrimalItemConsumable_CookedMeat_Jerky", "熟肉干");
        all.put("PrimalItemConsumable_CookedPrimeMeat_Fish", "优质熟肉");
        all.put("PrimalItemConsumable_CookedPrimeMeat_Jerky", "优质熟肉干");
        all.put("PrimalItemConsumable_Soup_EnduroStew", "耐力汤");

        all.put("PrimalItem_WeaponC4", "C4遥控器");
        all.put("PrimalItem_WeaponCompoundBow", "复合弓");
        all.put("PrimalItem_WeaponMachinedShotgun", "泵式霰弹枪");
        all.put("PrimalItem_WeaponMachinedSniper", "制式狙击步枪");
        all.put("PrimalItem_WeaponProd", "电击棍");
        all.put("PrimalItem_WeaponRifle", "制式步枪");
        all.put("PrimalItem_WeaponRocketLauncher", "RPG");
        all.put("PrimalItemAmmo_AdvancedRifleBullet", "高级步枪子弹");
        all.put("PrimalItemAmmo_AdvancedSniperBullet", "制式狙击步枪子弹");
        all.put("PrimalItemAmmo_CompoundBowArrow", "金属箭");
        all.put("PrimalItemAmmo_Rocket", "火箭弹");
        all.put("PrimalItemC4Ammo", "C4炸弹");

        all.put("PrimalItemArmor_ProcoptodonSaddle", "巨型袋鼠鞍");

        all.put("PrimalItem_WeaponGun", "简易手枪");

        all.put("PrimalItemArmor_RiotBoots", "防暴鞋子");
        all.put("PrimalItemArmor_RiotGloves", "防暴手套");
        all.put("PrimalItemArmor_RiotHelmet", "防暴帽子");
        all.put("PrimalItemArmor_RiotPants", "防暴裤子");
        all.put("PrimalItemArmor_RiotShirt", "防暴上衣");
        all.put("PrimalItemArmor_TransparentRiotShield", "防爆盾");

        all.put("PrimalItemArmor_GigantoraptorSaddle", "巨盗龙鞍");
        all.put("PrimalItemArmor_CarchaSaddle", "鲨齿龙鞍");
        all.put("PrimalItemArmor_QuetzSaddle", "风神翼龙鞍");
        all.put("PrimalItemArmor_QuetzSaddle_Platform", "风神翼龙平台鞍");
        all.put("PrimalItemArmor_TherizinosaurusSaddle", "镰刀龙鞍");
        all.put("PrimalItemArmor_YutySaddle", "羽暴龙鞍");
        all.put("PrimalItemConsumable_Soup_LazarusChowder", "拉撒路杂烩");
        all.put("PrimalItemConsumable_Soup_ShadowSteak", "暗影牛排");

        all.put("PrimalItemArmor_ChitinBoots", "甲壳鞋子");
        all.put("PrimalItemArmor_ChitinGloves", "甲壳手套");
        all.put("PrimalItemArmor_ChitinHelmet", "甲壳帽子");
        all.put("PrimalItemArmor_ChitinPants", "甲壳裤子");
        all.put("PrimalItemArmor_ChitinShirt", "甲壳上衣");

        all.put("PrimalItem_WeaponLance", "长茅");
        all.put("PrimalItemArmor_Deinosuchus_Saddle_ASA", "恐鳄鞍");
        all.put("PrimalItemArmor_RhynioSaddle", "莱尼虫鞍");
        all.put("PrimalItemArmor_RexSaddle", "霸王龙鞍");
        all.put("PrimalItemArmor_GigantSaddle", "南方巨兽龙鞍");
        all.put("PrimalItemArmor_SauroSaddle_Platform", "雷龙平台鞍");

        all.put("PrimalItemArmor_ScubaBoots_Flippers", "潜水鞋");
        all.put("PrimalItemArmor_ScubaShirt_SuitWithTank", "潜水服");
        all.put("PrimalItemArmor_ScubaHelmet_Goggles", "潜水面具");
        all.put("PrimalItemArmor_ScubaPants", "潜水裤子");

        all.put("PrimalItemArmor_SpinoSaddle", "棘背龙鞍");
        all.put("PrimalItemArmor_TusoSaddle", "托斯特巨鱿鞍");
        all.put("PrimalItemArmor_MosaSaddle", "沧龙鞍");
        all.put("PrimalItemArmor_PlesiSaddle_Platform", "蛇颈龙平台鞍");
        all.put("PrimalItemArmor_MosaSaddle_Platform", "沧龙平台鞍");
        all.put("PrimalItemArmor_MinersHelmet", "沉重的矿工安全帽");


        /* 焦土 */
        all.put("PrimalItemArmor_CamelsaurusSaddle", "驼峰兽鞍");
        all.put("PrimalItemArmor_MantisSaddle", "螳螂鞍");
        all.put("PrimalItemArmor_MothSaddle", "沙蛾鞍");
        all.put("PrimalItemArmor_DesertClothGogglesHelmet", "沙漠帽子");
        all.put("PrimalItemArmor_DesertClothBoots", "沙漠鞋子");
        all.put("PrimalItemArmor_DesertClothShirt", "沙漠上衣");
        all.put("PrimalItemArmor_DesertClothPants", "沙漠裤子");
        all.put("PrimalItemArmor_DesertClothGloves", "沙漠手套");
        all.put("PrimalItem_WeaponBoomerang", "回旋镖");
        all.put("PrimalItem_WeaponWhip", "鞭子");
        all.put("PrimalItemConsumable_CactusBuffSoup", "仙人掌汁");
        all.put("PrimalItem_ChainSaw", "电锯");
        all.put("PrimalItem_WeaponOilJar", "油罐");
        all.put("PrimalItemAmmo_Flamethrower", "火焰喷射器弹药");
        all.put("PrimalItemAmmo_RocketHomingMissile", "制导火箭弹");
        all.put("PrimalItem_WeapFlamethrower", "火焰喷射器");


        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_ClothBoots");
        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_ClothGloves");
        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_ClothHelmet");
        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_ClothPants");
        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_ClothShirt");
        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_HideBoots");
        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_HideGloves");
        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_HideHelmet");
        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_HidePants");
        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_HideShirt");
        LootItemSet_CaveDrop_T1_Armor_ASA.add("PrimalItemArmor_WoodShield");

        LootItemSet_CaveDrop_T1_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_DolphinSaddle",
                "PrimalItemArmor_EquusSaddle",
                "PrimalItemArmor_Gallimimus",
                "PrimalItemArmor_HyaenodonSaddle",
                "PrimalItemArmor_MantaSaddle",
                "PrimalItemArmor_PachyrhinoSaddle",
                "PrimalItemArmor_PachySaddle",
                "PrimalItemArmor_ParaSaddle",
                "PrimalItemArmor_PhiomiaSaddle",
                "PrimalItemArmor_RaptorSaddle",
                "PrimalItemArmor_SarcoSaddle",
                "PrimalItemArmor_ScorpionSaddle",
                "PrimalItemArmor_StegoSaddle",
                "PrimalItemArmor_TrikeSaddle",
                "PrimalItemArmor_TurtleSaddle",
                "PrimalItemConsumable_Berry_Stimberry",
                "PrimalItemConsumable_CookedMeat"
        ));

        LootItemSet_CaveDrop_T1_Weapons_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemResource_Stone",
                "PrimalItem_GasGrenade",
                "PrimalItem_WeaponBola",
                "PrimalItem_WeaponBow",
                "PrimalItem_WeaponFlareGun",
                "PrimalItem_WeaponPaintbrush",
                "PrimalItem_WeaponScissors",
                "PrimalItem_WeaponSlingshot",
                "PrimalItem_WeaponSpear",
                "PrimalItem_WeaponStoneClub",
                "PrimalItem_WeaponStoneHatchet",
                "PrimalItem_WeaponStonePick",
                "PrimalItem_WeaponTorch",
                "PrimalItemAmmo_ArrowStone",
                "PrimalItemAmmo_ArrowTranq",
                "PrimalItem_WeaponMagnifyingGlass",
                "PrimalItem_WeaponSpyglass"
        ));

        LootItemSet_CaveDrop_T2_Armor_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_ChitinBoots",
                "PrimalItemArmor_ChitinGloves",
                "PrimalItemArmor_ChitinHelmet",
                "PrimalItemArmor_ChitinPants",
                "PrimalItemArmor_ChitinShirt"
        ));

        LootItemSet_CaveDrop_T2_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_AnkyloSaddle",
                "PrimalItemArmor_ArthroSaddle",
                "PrimalItemArmor_CarnoSaddle",
                "PrimalItemArmor_DiplodocusSaddle",
                "PrimalItemArmor_DireBearSaddle",
                "PrimalItemArmor_DoedSaddle",
                "PrimalItemArmor_DunkleosteusSaddle",
                "PrimalItemArmor_IguanodonSaddle",
                "PrimalItemArmor_MammothSaddle",
                "PrimalItemArmor_MegatheriumSaddle",
                "PrimalItemArmor_SaberSaddle",
                "PrimalItemArmor_SpiderSaddle",
                "PrimalItemArmor_StagSaddle",
                "PrimalItemArmor_TerrorBirdSaddle",
                "PrimalItemConsumable_CookedMeat",
                "PrimalItemConsumable_CookedMeat_Fish"
        ));

        LootItemSet_CaveDrop_T2_Weapons_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItem_WeaponCrossbow",
                "PrimalItem_WeaponFlareGun",
                "PrimalItem_WeaponGrenade",
                "PrimalItem_WeaponMetalHatchet",
                "PrimalItem_WeaponMetalPick",
                "PrimalItem_WeaponPike",
                "PrimalItem_WeaponShotgun",
                "PrimalItem_WeaponSickle",
                "PrimalItem_WeaponSword",
                "PrimalItemAmmo_ArrowStone",
                "PrimalItemAmmo_ArrowTranq",
                "PrimalItemAmmo_SimpleBullet",
                "PrimalItemAmmo_SimpleShotgunBullet"
        ));

        LootItemSet_CaveDrop_T3_Armor_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_FurBoots",
                "PrimalItemArmor_FurGloves",
                "PrimalItemArmor_FurHelmet",
                "PrimalItemArmor_FurPants",
                "PrimalItemArmor_FurShirt",
                "PrimalItemArmor_GhillieBoots",
                "PrimalItemArmor_GhillieGloves",
                "PrimalItemArmor_GhillieHelmet",
                "PrimalItemArmor_GhilliePants",
                "PrimalItemArmor_GhillieShirt"
        ));

        LootItemSet_CaveDrop_T3_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_AlloSaddle",
                "PrimalItemArmor_ChalicoSaddle",
                "PrimalItemArmor_KaprosuchusSaddle",
                "PrimalItemArmor_MegalaniaSaddle",
                "PrimalItemArmor_Paracer_Saddle",
                "PrimalItemArmor_PelaSaddle",
                "PrimalItemArmor_PlesiaSaddle",
                "PrimalItemArmor_PteroSaddle",
                "PrimalItemArmor_SauroSaddle",
                "PrimalItemArmor_ToadSaddle",
                "PrimalItemConsumable_HealSoup",
                "PrimalItemConsumable_Soup_CalienSoup",
                "PrimalItemConsumable_Soup_FocalChili",
                "PrimalItemConsumable_Soup_FriaCurry",
                "PrimalItemConsumable_StaminaSoup"
        ));

        LootItemSet_CaveDrop_T3_Weapons_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItem_PoisonGrenade",
                "PrimalItem_WeaponHarpoon",
                "PrimalItem_WeaponMachinedPistol",
                "PrimalItem_WeaponOneShotRifle",
                "PrimalItem_WeaponTripwireC4",
                "PrimalItemAmmo_AdvancedBullet",
                "PrimalItemAmmo_BallistaArrow",
                "PrimalItemAmmo_SimpleRifleBullet"
        ));

        LootItemSet_CaveDrop_T4_Armor_ASA.addAll(CollectionUtil.newArrayList(

                "PrimalItemArmor_MetalBoots",
                "PrimalItemArmor_MetalGloves",
                "PrimalItemArmor_MetalHelmet",
                "PrimalItemArmor_MetalPants",
                "PrimalItemArmor_MetalShirt",
                "PrimalItemArmor_MetalShield"
        ));

        LootItemSet_CaveDrop_T4_Saddles_ASA.addAll(CollectionUtil.newArrayList(

                "PrimalItem_Armor_Archelon_Saddle_ASA",
                "PrimalItemArmor_CeratosaurusSaddle_ASA",
                "PrimalItemArmor_DeinotheriumSaddle_ASA",
                "PrimalItemArmor_XiphSaddle_ASA",
                "PrimalItemArmor_ArgentavisSaddle",
                "PrimalItemArmor_BaryonyxSaddle",
                "PrimalItemArmor_BasiloSaddle",
                "PrimalItemArmor_BeaverSaddle",
                "PrimalItemArmor_DaeodonSaddle",
                "PrimalItemArmor_MegalodonSaddle",
                "PrimalItemArmor_MegalosaurusSaddle",
                "PrimalItemArmor_ParacerSaddle_Platform",
                "PrimalItemArmor_RhinoSaddle",
                "PrimalItemArmor_TapejaraSaddle",
                "PrimalItemArmor_ThylacoSaddle",
                "PrimalItemConsumable_CookedMeat_Jerky",
                "PrimalItemConsumable_CookedPrimeMeat_Fish",
                "PrimalItemConsumable_CookedPrimeMeat_Jerky",
                "PrimalItemConsumable_Soup_EnduroStew",
                "PrimalItemConsumable_Soup_LazarusChowder",
                "PrimalItemConsumable_Soup_ShadowSteak"

        ));

        LootItemSet_CaveDrop_T4_Weapons_ASA.addAll(CollectionUtil.newArrayList(

                "PrimalItem_WeaponC4",
                "PrimalItem_WeaponCompoundBow",
                "PrimalItem_WeaponMachinedShotgun",
                "PrimalItem_WeaponMachinedSniper",
                "PrimalItem_WeaponProd",
                "PrimalItem_WeaponRifle",
                "PrimalItem_WeaponRocketLauncher",
                "PrimalItemAmmo_AdvancedRifleBullet",
                "PrimalItemAmmo_AdvancedSniperBullet",
                "PrimalItemAmmo_CompoundBowArrow",
                "PrimalItemAmmo_Rocket",
                "PrimalItemAmmo_SimpleShotgunBullet",
                "PrimalItemC4Ammo"
        ));

        LootItemSet_CaveDrop_Ice_T1_Armor_ASA.addAll(CollectionUtil.newArrayList(

                "PrimalItemArmor_FurBoots",
                "PrimalItemArmor_FurGloves",
                "PrimalItemArmor_FurHelmet",
                "PrimalItemArmor_FurPants",
                "PrimalItemArmor_FurShirt"
        ));

        LootItemSet_CaveDrop_Ice_T1_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_CarnoSaddle",
                "PrimalItemArmor_EquusSaddle",
                "PrimalItemArmor_HyaenodonSaddle",
                "PrimalItemArmor_MammothSaddle",
                "PrimalItemArmor_MegatheriumSaddle",
                "PrimalItemArmor_ProcoptodonSaddle",
                "PrimalItemArmor_RaptorSaddle",
                "PrimalItemArmor_StagSaddle",
                "PrimalItemArmor_StegoSaddle",
                "PrimalItemArmor_TerrorBirdSaddle",
                "PrimalItemArmor_TrikeSaddle",
                "PrimalItemConsumable_CookedMeat",
                "PrimalItemConsumable_CookedMeat_Fish"));

        LootItemSet_CaveDrop_Ice_T1_Weapons_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemResource_Stone",
                "PrimalItem_WeaponCrossbow",
                "PrimalItem_WeaponGun",
                "PrimalItem_WeaponMetalHatchet",
                "PrimalItem_WeaponMetalPick",
                "PrimalItem_WeaponPike",
                "PrimalItem_WeaponSickle",
                "PrimalItem_WeaponSword",
                "PrimalItem_WeaponTorch",
                "PrimalItemAmmo_ArrowStone",
                "PrimalItemAmmo_ArrowTranq",
                "PrimalItemAmmo_SimpleBullet"));

        LootItemSet_CaveDrop_Ice_T2_Armor_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_MetalBoots",
                "PrimalItemArmor_MetalGloves",
                "PrimalItemArmor_MetalHelmet",
                "PrimalItemArmor_MetalPants",
                "PrimalItemArmor_MetalShirt",
                "PrimalItemArmor_MetalShield"));

        LootItemSet_CaveDrop_Ice_T2_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_ArgentavisSaddle",
                "PrimalItemArmor_BeaverSaddle",
                "PrimalItemArmor_ChalicoSaddle",
                "PrimalItemArmor_DireBearSaddle",
                "PrimalItemArmor_MegalosaurusSaddle",
                "PrimalItemArmor_RhinoSaddle",
                "PrimalItemArmor_SaberSaddle",
                "PrimalItemArmor_ThylacoSaddle",
                "PrimalItemConsumable_HealSoup",
                "PrimalItemConsumable_Soup_CalienSoup",
                "PrimalItemConsumable_Soup_FocalChili",
                "PrimalItemConsumable_Soup_FriaCurry",
                "PrimalItemConsumable_StaminaSoup"));


        LootItemSet_CaveDrop_Ice_T2_Weapons_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItem_WeaponMachinedPistol",
                "PrimalItem_WeaponOneShotRifle",
                "PrimalItem_WeaponRifle",
                "PrimalItem_WeaponShotgun",
                "PrimalItemAmmo_AdvancedBullet",
                "PrimalItemAmmo_AdvancedRifleBullet",
                "PrimalItemAmmo_SimpleRifleBullet",
                "PrimalItemAmmo_SimpleShotgunBullet"));

        LootItemSet_CaveDrop_Ice_T3_Armor_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_RiotBoots",
                "PrimalItemArmor_RiotGloves",
                "PrimalItemArmor_RiotHelmet",
                "PrimalItemArmor_RiotPants",
                "PrimalItemArmor_RiotShirt",
                "PrimalItemArmor_TransparentRiotShield"));

        LootItemSet_CaveDrop_Ice_T3_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_GigantoraptorSaddle",
                "PrimalItemArmor_CarchaSaddle",
                "PrimalItemArmor_DaeodonSaddle",
                "PrimalItemArmor_QuetzSaddle",
                "PrimalItemArmor_QuetzSaddle_Platform",
                "PrimalItemArmor_TherizinosaurusSaddle",
                "PrimalItemArmor_YutySaddle",
                "PrimalItemConsumable_CookedMeat_Jerky",
                "PrimalItemConsumable_CookedPrimeMeat_Fish",
                "PrimalItemConsumable_CookedPrimeMeat_Jerky",
                "PrimalItemConsumable_Soup_EnduroStew",
                "PrimalItemConsumable_Soup_LazarusChowder",
                "PrimalItemConsumable_Soup_ShadowSteak"));

        LootItemSet_CaveDrop_Ice_T3_Weapons_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItem_WeaponCompoundBow",
                "PrimalItem_WeaponMachinedShotgun",
                "PrimalItem_WeaponMachinedSniper",
                "PrimalItem_WeaponProd",
                "PrimalItem_WeaponRifle",
                "PrimalItem_WeaponRocketLauncher",
                "PrimalItemAmmo_AdvancedRifleBullet",
                "PrimalItemAmmo_AdvancedSniperBullet",
                "PrimalItemAmmo_CompoundBowArrow",
                "PrimalItemAmmo_Rocket",
                "PrimalItemAmmo_SimpleShotgunBullet"));

        LootItemSet_CaveDrop_Swamp_T1_Armor_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_GhillieBoots",
                "PrimalItemArmor_GhillieGloves",
                "PrimalItemArmor_GhillieHelmet",
                "PrimalItemArmor_GhilliePants",
                "PrimalItemArmor_GhillieShirt"));

        LootItemSet_CaveDrop_Swamp_T1_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_ArthroSaddle",
                "PrimalItemArmor_CarnoSaddle",
                "PrimalItemArmor_IguanodonSaddle",
                "PrimalItemArmor_KaprosuchusSaddle",
                "PrimalItemArmor_RaptorSaddle",
                "PrimalItemArmor_SarcoSaddle",
                "PrimalItemArmor_ScorpionSaddle",
                "PrimalItemArmor_SpiderSaddle",
                "PrimalItemArmor_StegoSaddle",
                "PrimalItemArmor_ToadSaddle",
                "PrimalItemArmor_TrikeSaddle",
                "PrimalItemConsumable_CookedMeat",
                "PrimalItemConsumable_CookedMeat_Fish"));

        LootItemSet_CaveDrop_Swamp_T1_Weapons_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemResource_Stone",
                "PrimalItem_WeaponCrossbow",
                "PrimalItem_WeaponGun",
                "PrimalItem_WeaponMetalHatchet",
                "PrimalItem_WeaponMetalPick",
                "PrimalItem_WeaponPike",
                "PrimalItem_WeaponSickle",
                "PrimalItem_WeaponSword",
                "PrimalItem_WeaponTorch",
                "PrimalItemAmmo_ArrowStone",
                "PrimalItemAmmo_ArrowTranq",
                "PrimalItemAmmo_SimpleBullet"));

        LootItemSet_CaveDrop_Swamp_T2_Armor_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_MetalBoots",
                "PrimalItemArmor_MetalGloves",
                "PrimalItemArmor_MetalHelmet",
                "PrimalItemArmor_MetalPants",
                "PrimalItemArmor_MetalShirt",
                "PrimalItemArmor_MetalShield"));

        LootItemSet_CaveDrop_Swamp_T2_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_Deinosuchus_Saddle_ASA",
                "PrimalItemArmor_AlloSaddle",
                "PrimalItemArmor_AnkyloSaddle",
                "PrimalItemArmor_DoedSaddle",
                "PrimalItemArmor_MegalaniaSaddle",
                "PrimalItemArmor_Paracer_Saddle",
                "PrimalItemArmor_ParacerSaddle_Platform",
                "PrimalItemArmor_PteroSaddle",
                "PrimalItemArmor_TapejaraSaddle",
                "PrimalItemConsumable_HealSoup",
                "PrimalItemConsumable_Soup_CalienSoup",
                "PrimalItemConsumable_Soup_FocalChili",
                "PrimalItemConsumable_Soup_FriaCurry",
                "PrimalItemConsumable_StaminaSoup"));

        LootItemSet_CaveDrop_Swamp_T2_Weapons_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItem_WeaponMachinedPistol",
                "PrimalItem_WeaponOneShotRifle",
                "PrimalItem_WeaponRifle",
                "PrimalItem_WeaponShotgun",
                "PrimalItemAmmo_AdvancedBullet",
                "PrimalItemAmmo_AdvancedRifleBullet",
                "PrimalItemAmmo_SimpleRifleBullet",
                "PrimalItemAmmo_SimpleShotgunBullet"));

        LootItemSet_CaveDrop_Swamp_T3_Armor_ASA.addAll(CollectionUtil.newArrayList("PrimalItemArmor_RiotBoots",
                "PrimalItemArmor_RiotGloves",
                "PrimalItemArmor_RiotHelmet",
                "PrimalItemArmor_RiotPants",
                "PrimalItemArmor_RiotShirt",
                "PrimalItemArmor_TransparentRiotShield"));

        LootItemSet_CaveDrop_Swamp_T3_Saddles_ASA.addAll(CollectionUtil.newArrayList("PrimalItemArmor_DeinotheriumSaddle_ASA",
                "PrimalItemArmor_GigantSaddle",
                "PrimalItemArmor_RexSaddle",
                "PrimalItemArmor_RhynioSaddle",
                "PrimalItemArmor_SauroSaddle",
                "PrimalItemArmor_SauroSaddle_Platform",
                "PrimalItemConsumable_CookedMeat_Jerky",
                "PrimalItemConsumable_CookedPrimeMeat_Fish",
                "PrimalItemConsumable_CookedPrimeMeat_Jerky",
                "PrimalItemConsumable_Soup_EnduroStew",
                "PrimalItemConsumable_Soup_LazarusChowder",
                "PrimalItemConsumable_Soup_ShadowSteak"));

        LootItemSet_CaveDrop_Swamp_T3_Weapons_ASA.addAll(CollectionUtil.newArrayList("PrimalItem_WeaponCompoundBow",
                "PrimalItem_WeaponMachinedShotgun",
                "PrimalItem_WeaponMachinedSniper",
                "PrimalItem_WeaponProd",
                "PrimalItem_WeaponRifle",
                "PrimalItem_WeaponRocketLauncher",
                "PrimalItemAmmo_AdvancedRifleBullet",
                "PrimalItemAmmo_AdvancedSniperBullet",
                "PrimalItemAmmo_CompoundBowArrow",
                "PrimalItemAmmo_Rocket",
                "PrimalItemAmmo_SimpleShotgunBullet"));

        LootItemSet_CaveDrop_T1_Armor_ScorchedEarth.addAll(CollectionUtil.newArrayList("PrimalItemArmor_HideBoots",
                "PrimalItemArmor_HideGloves",
                "PrimalItemArmor_HideHelmet",
                "PrimalItemArmor_HidePants",
                "PrimalItemArmor_HideShirt",
                "PrimalItemArmor_WoodShield",
                "PrimalItemArmor_DesertClothBoots",
                "PrimalItemArmor_DesertClothGloves",
                "PrimalItemArmor_DesertClothGogglesHelmet",
                "PrimalItemArmor_DesertClothPants",
                "PrimalItemArmor_DesertClothShirt"));

        LootItemSet_CaveDrop_T1_Saddles_ScorchedEarth.addAll(CollectionUtil.newArrayList("PrimalItemArmor_EquusSaddle",
                "PrimalItemArmor_Gallimimus",
                "PrimalItemArmor_HyaenodonSaddle",
                "PrimalItemArmor_ParaSaddle",
                "PrimalItemArmor_RaptorSaddle",
                "PrimalItemArmor_ScorpionSaddle",
                "PrimalItemArmor_CamelsaurusSaddle",
                "PrimalItemArmor_MantisSaddle",
                "PrimalItemArmor_MothSaddle"));

        LootItemSet_CaveDrop_T1_Weapons_ScorchedEarth.addAll(CollectionUtil.newArrayList("PrimalItemResource_Stone",
                "PrimalItem_GasGrenade",
                "PrimalItem_WeaponBola",
                "PrimalItem_WeaponBow",
                "PrimalItem_WeaponFlareGun",
                "PrimalItem_WeaponPaintbrush",
                "PrimalItem_WeaponScissors",
                "PrimalItem_WeaponSlingshot",
                "PrimalItem_WeaponSpear",
                "PrimalItem_WeaponStoneClub",
                "PrimalItem_WeaponStoneHatchet",
                "PrimalItem_WeaponStonePick",
                "PrimalItem_WeaponTorch",
                "PrimalItemAmmo_ArrowStone",
                "PrimalItemAmmo_ArrowTranq",
                "PrimalItem_WeaponMagnifyingGlass",
                "PrimalItem_WeaponSpyglass",
                "PrimalItem_WeaponBoomerang",
                "PrimalItem_WeaponWhip"));

        LootItemSet_CaveDrop_T2_Saddles_ScorchedEarth.addAll(CollectionUtil.newArrayList("PrimalItemArmor_AnkyloSaddle",
                "PrimalItemArmor_ArthroSaddle",
                "PrimalItemArmor_CarnoSaddle",
                "PrimalItemArmor_DoedSaddle",
                "PrimalItemArmor_IguanodonSaddle",
                "PrimalItemArmor_MegatheriumSaddle",
                "PrimalItemArmor_SaberSaddle",
                "PrimalItemArmor_SpiderSaddle",
                "PrimalItemArmor_TerrorBirdSaddle",
                "PrimalItemConsumable_CactusBuffSoup",
                "PrimalItemConsumable_CookedMeat",
                "PrimalItemConsumable_CookedMeat_Fish",
                "PrimalItemArmor_CamelsaurusSaddle",
                "PrimalItemArmor_MantisSaddle",
                "PrimalItemArmor_MothSaddle"));

        LootItemSet_CaveDrop_T2_Weapons_ScorchedEarth.addAll(CollectionUtil.newArrayList("PrimalItem_WeaponCrossbow",
                "PrimalItem_WeaponFlareGun",
                "PrimalItem_WeaponGrenade",
                "PrimalItem_WeaponMetalHatchet",
                "PrimalItem_WeaponMetalPick",
                "PrimalItem_WeaponPike",
                "PrimalItem_WeaponShotgun",
                "PrimalItem_WeaponSickle",
                "PrimalItem_WeaponSword",
                "PrimalItemAmmo_ArrowStone",
                "PrimalItemAmmo_ArrowTranq",
                "PrimalItemAmmo_SimpleBullet",
                "PrimalItemAmmo_SimpleShotgunBullet",
                "PrimalItem_ChainSaw",
                "PrimalItem_WeaponOilJar"));

        LootItemSet_CaveDrop_T3_Saddles_ScorchedEarth.addAll(CollectionUtil.newArrayList("PrimalItemArmor_Deinosuchus_Saddle_ASA",
                "PrimalItemArmor_DeinotheriumSaddle_ASA",
                "PrimalItemArmor_FasolaSaddle",
                "PrimalItemArmor_CarnoSaddle",
                "PrimalItemArmor_KaprosuchusSaddle",
                "PrimalItemArmor_MegalaniaSaddle",
                "PrimalItemArmor_Paracer_Saddle",
                "PrimalItemConsumable_CactusBuffSoup",
                "PrimalItemConsumable_HealSoup",
                "PrimalItemConsumable_Soup_CalienSoup",
                "PrimalItemConsumable_Soup_FocalChili",
                "PrimalItemConsumable_Soup_FriaCurry",
                "PrimalItemConsumable_StaminaSoup",
                "PrimalItemArmor_RockGolemSaddle"));

        LootItemSet_CaveDrop_T3_Weapons_ScorchedEarth.addAll(CollectionUtil.newArrayList("PrimalItem_PoisonGrenade",
                "PrimalItem_WeaponHarpoon",
                "PrimalItem_WeaponMachinedPistol",
                "PrimalItem_WeaponOneShotRifle",
                "PrimalItem_WeaponTripwireC4",
                "PrimalItemAmmo_AdvancedBullet",
                "PrimalItemAmmo_BallistaArrow",
                "PrimalItemAmmo_SimpleRifleBullet",
                "PrimalItem_WeapFlamethrower",
                "PrimalItemAmmo_Flamethrower",
                "PrimalItemAmmo_RocketHomingMissile"));

        LootItemSet_CaveDrop_T1_Armor.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_ClothBoots",
                "PrimalItemArmor_ClothGloves",
                "PrimalItemArmor_ClothHelmet",
                "PrimalItemArmor_ClothPants",
                "PrimalItemArmor_ClothShirt"
        ));


        LootItemSet_CaveDrop_T1_Saddles.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_DolphinSaddle",
                "PrimalItemArmor_EquusSaddle",
                "PrimalItemArmor_HyaenodonSaddle",
                "PrimalItemArmor_PachySaddle",
                "PrimalItemArmor_ParaSaddle",
                "PrimalItemArmor_PhiomiaSaddle",
                "PrimalItemArmor_ProcoptodonSaddle",
                "PrimalItemArmor_RaptorSaddle",
                "PrimalItemArmor_ScorpionSaddle",
                "PrimalItemArmor_TrikeSaddle",
                "PrimalItemArmor_TurtleSaddle"
        ));

        LootItemSet_CaveDrop_T1_Weapons.addAll(CollectionUtil.newArrayList(
                "PrimalItem_WeaponBow",
                "PrimalItem_WeaponSlingshot",
                "PrimalItem_WeaponStoneClub",
                "PrimalItem_WeaponStoneHatchet",
                "PrimalItem_WeaponStonePick"
        ));

        LootItemSet_CaveDrop_T2_Armor.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_FurBoots",
                "PrimalItemArmor_FurGloves",
                "PrimalItemArmor_FurHelmet",
                "PrimalItemArmor_FurPants",
                "PrimalItemArmor_FurShirt",
                "PrimalItemArmor_HideBoots",
                "PrimalItemArmor_HideGloves",
                "PrimalItemArmor_HideHelmet",
                "PrimalItemArmor_HidePants",
                "PrimalItemArmor_HideShirt",
                "PrimalItemArmor_WoodShield"
        ));

        LootItemSet_CaveDrop_T2_Saddles.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_AnkyloSaddle",
                "PrimalItemArmor_DiplodocusSaddle",
                "PrimalItemArmor_DoedSaddle",
                "PrimalItemArmor_Gallimimus",
                "PrimalItemArmor_IguanodonSaddle",
                "PrimalItemArmor_KaprosuchusSaddle",
                "PrimalItemArmor_MammothSaddle",
                "PrimalItemArmor_MantaSaddle",
                "PrimalItemArmor_PachyrhinoSaddle",
                "PrimalItemArmor_PteroSaddle",
                "PrimalItemArmor_SarcoSaddle",
                "PrimalItemArmor_StagSaddle",
                "PrimalItemArmor_StegoSaddle",
                "PrimalItemArmor_TerrorBirdSaddle"
        ));

        LootItemSet_CaveDrop_T2_Weapons.addAll(CollectionUtil.newArrayList(
                "PrimalItem_WeaponCrossbow",
                "PrimalItem_WeaponGun",
                "PrimalItem_WeaponLance",
                "PrimalItem_WeaponMetalHatchet",
                "PrimalItem_WeaponMetalPick",
                "PrimalItem_WeaponOneShotRifle",
                "PrimalItem_WeaponPike",
                "PrimalItem_WeaponShotgun",
                "PrimalItem_WeaponSickle"
        ));


        LootItemSet_CaveDrop_Underwater_T1_Armor_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_ChitinBoots",
                "PrimalItemArmor_ChitinGloves",
                "PrimalItemArmor_ChitinHelmet",
                "PrimalItemArmor_ChitinPants",
                "PrimalItemArmor_ChitinShirt"
        ));

        LootItemSet_CaveDrop_Underwater_T1_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_CarnoSaddle",
                "PrimalItemArmor_DiplodocusSaddle",
                "PrimalItemArmor_DolphinSaddle",
                "PrimalItemArmor_ParaSaddle",
                "PrimalItemArmor_RaptorSaddle",
                "PrimalItemArmor_StegoSaddle",
                "PrimalItemArmor_TrikeSaddle",
                "PrimalItemConsumable_HealSoup",
                "PrimalItemConsumable_Soup_CalienSoup",
                "PrimalItemConsumable_Soup_FocalChili",
                "PrimalItemConsumable_Soup_FriaCurry",
                "PrimalItemConsumable_StaminaSoup"
        ));

        LootItemSet_CaveDrop_Underwater_T2_Armor_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItem_WeaponCrossbow",
                "PrimalItem_WeaponGun",
                "PrimalItem_WeaponLance",
                "PrimalItem_WeaponMetalHatchet",
                "PrimalItem_WeaponMetalPick",
                "PrimalItem_WeaponOneShotRifle",
                "PrimalItem_WeaponPike",
                "PrimalItem_WeaponShotgun",
                "PrimalItem_WeaponSickle"
        ));

        LootItemSet_CaveDrop_Underwater_T2_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_ScubaBoots_Flippers",
                "PrimalItemArmor_ScubaHelmet_Goggles",
                "PrimalItemArmor_ScubaPants",
                "PrimalItemArmor_ScubaShirt_SuitWithTank"

        ));

        LootItemSet_CaveDrop_Underwater_T3_Armor_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_MetalBoots",
                "PrimalItemArmor_MetalGloves",
                "PrimalItemArmor_MetalHelmet",
                "PrimalItemArmor_MetalPants",
                "PrimalItemArmor_MetalShirt",
                "PrimalItemArmor_MetalShield"
        ));

        LootItemSet_CaveDrop_Underwater_T3_Saddles_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItemArmor_MosaSaddle",
                "PrimalItemArmor_MosaSaddle_Platform",
                "PrimalItemArmor_PlesiaSaddle",
                "PrimalItemArmor_PlesiSaddle_Platform",
                "PrimalItemArmor_SpinoSaddle",
                "PrimalItemArmor_TusoSaddle",
                "PrimalItemConsumable_HealSoup",
                "PrimalItemConsumable_Soup_CalienSoup",
                "PrimalItemConsumable_Soup_FocalChili",
                "PrimalItemConsumable_Soup_FriaCurry",
                "PrimalItemConsumable_StaminaSoup"
        ));


        LootItemSet_SupplyDrop_Ocean_ASA.addAll(CollectionUtil.newArrayList(
                "PrimalItem_Armor_Archelon_Saddle_ASA",
                "PrimalItemArmor_CeratosaurusSaddle_ASA",
                "PrimalItemArmor_DeinotheriumSaddle_ASA",
                "PrimalItemArmor_GigantoraptorSaddle",
                "PrimalItemArmor_XiphSaddle_ASA",
                "PrimalItemArmor_ChitinBoots",
                "PrimalItemArmor_ChitinGloves",
                "PrimalItemArmor_ChitinHelmet",
                "PrimalItemArmor_ChitinPants",
                "PrimalItemArmor_ChitinShirt",
                "PrimalItemArmor_ClothBoots",
                "PrimalItemArmor_ClothGloves",
                "PrimalItemArmor_ClothHelmet",
                "PrimalItemArmor_ClothPants",
                "PrimalItemArmor_ClothShirt",
                "PrimalItemArmor_FurBoots",
                "PrimalItemArmor_FurGloves",
                "PrimalItemArmor_FurHelmet",
                "PrimalItemArmor_FurPants",
                "PrimalItemArmor_FurShirt",
                "PrimalItemArmor_GhillieBoots",
                "PrimalItemArmor_GhillieGloves",
                "PrimalItemArmor_GhillieHelmet",
                "PrimalItemArmor_GhilliePants",
                "PrimalItemArmor_GhillieShirt",
                "PrimalItemArmor_HideBoots",
                "PrimalItemArmor_HideGloves",
                "PrimalItemArmor_HideHelmet",
                "PrimalItemArmor_HidePants",
                "PrimalItemArmor_HideShirt",
                "PrimalItemArmor_MetalBoots",
                "PrimalItemArmor_MetalGloves",
                "PrimalItemArmor_MetalHelmet",
                "PrimalItemArmor_MetalPants",
                "PrimalItemArmor_MetalShirt",
                "PrimalItemArmor_MinersHelmet",
                "PrimalItemArmor_AlloSaddle",
                "PrimalItemArmor_AnkyloSaddle",
                "PrimalItemArmor_ArgentavisSaddle",
                "PrimalItemArmor_ArthroSaddle",
                "PrimalItemArmor_BaryonyxSaddle",
                "PrimalItemArmor_BasiloSaddle",
                "PrimalItemArmor_BeaverSaddle",
                "PrimalItemArmor_CarchaSaddle",
                "PrimalItemArmor_CarnoSaddle",
                "PrimalItemArmor_ChalicoSaddle",
                "PrimalItemArmor_DaeodonSaddle",
                "PrimalItemArmor_DiplodocusSaddle",
                "PrimalItemArmor_DireBearSaddle",
                "PrimalItemArmor_DoedSaddle",
                "PrimalItemArmor_DolphinSaddle",
                "PrimalItemArmor_DunkleosteusSaddle",
                "PrimalItemArmor_EquusSaddle",
                "PrimalItemArmor_Gallimimus",
                "PrimalItemArmor_GigantSaddle",
                "PrimalItemArmor_HyaenodonSaddle",
                "PrimalItemArmor_IguanodonSaddle",
                "PrimalItemArmor_KaprosuchusSaddle",
                "PrimalItemArmor_MammothSaddle",
                "PrimalItemArmor_MantaSaddle",
                "PrimalItemArmor_MegalaniaSaddle",
                "PrimalItemArmor_MegalodonSaddle",
                "PrimalItemArmor_MegalosaurusSaddle",
                "PrimalItemArmor_MegatheriumSaddle",
                "PrimalItemArmor_MosaSaddle",
                "PrimalItemArmor_MosaSaddle_Platform",
                "PrimalItemArmor_PachyrhinoSaddle",
                "PrimalItemArmor_PachySaddle",
                "PrimalItemArmor_Paracer_Saddle",
                "PrimalItemArmor_ParacerSaddle_Platform",
                "PrimalItemArmor_ParaSaddle",
                "PrimalItemArmor_PelaSaddle",
                "PrimalItemArmor_PhiomiaSaddle",
                "PrimalItemArmor_PlesiaSaddle",
                "PrimalItemArmor_PlesiSaddle_Platform",
                "PrimalItemArmor_ProcoptodonSaddle",
                "PrimalItemArmor_PteroSaddle",
                "PrimalItemArmor_QuetzSaddle",
                "PrimalItemArmor_RaptorSaddle",
                "PrimalItemArmor_RexSaddle",
                "PrimalItemArmor_RhinoSaddle",
                "PrimalItemArmor_RhynioSaddle",
                "PrimalItemArmor_SaberSaddle",
                "PrimalItemArmor_SarcoSaddle",
                "PrimalItemArmor_SauroSaddle",
                "PrimalItemArmor_SauroSaddle_Platform",
                "PrimalItemArmor_ScorpionSaddle",
                "PrimalItemArmor_SpiderSaddle",
                "PrimalItemArmor_SpinoSaddle",
                "PrimalItemArmor_StagSaddle",
                "PrimalItemArmor_StegoSaddle",
                "PrimalItemArmor_TapejaraSaddle",
                "PrimalItemArmor_TerrorBirdSaddle",
                "PrimalItemArmor_TherizinosaurusSaddle",
                "PrimalItemArmor_ThylacoSaddle",
                "PrimalItemArmor_ToadSaddle",
                "PrimalItemArmor_TrikeSaddle",
                "PrimalItemArmor_TurtleSaddle",
                "PrimalItemArmor_TusoSaddle",
                "PrimalItemArmor_YutySaddle",
                "PrimalItemArmor_ScubaBoots_Flippers",
                "PrimalItemArmor_ScubaHelmet_Goggles",
                "PrimalItemArmor_ScubaPants",
                "PrimalItemArmor_ScubaShirt_SuitWithTank",
                "PrimalItemArmor_MetalShield",
                "PrimalItemArmor_WoodShield",
                "PrimalItem_WeaponBow",
                "PrimalItem_WeaponCompoundBow",
                "PrimalItem_WeaponCrossbow",
                "PrimalItem_WeaponGun",
                "PrimalItem_WeaponHarpoon",
                "PrimalItem_WeaponLance",
                "PrimalItem_WeaponMachinedPistol",
                "PrimalItem_WeaponMachinedShotgun",
                "PrimalItem_WeaponMachinedSniper",
                "PrimalItem_WeaponMetalHatchet",
                "PrimalItem_WeaponMetalPick",
                "PrimalItem_WeaponOneShotRifle",
                "PrimalItem_WeaponPike",
                "PrimalItem_WeaponProd",
                "PrimalItem_WeaponRifle",
                "PrimalItem_WeaponShotgun",
                "PrimalItem_WeaponSickle",
                "PrimalItem_WeaponSlingshot",
                "PrimalItem_WeaponStoneClub",
                "PrimalItem_WeaponSword",
                "PrimalItem_WeaponTorch"


        ));


        SupplyCrate_Cave_QualityTier1 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T1_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_T1_Saddles_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_T1_Weapons_ASA, 1)
                )
        );

        SupplyCrate_Cave_QualityTier2 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T1_Armor_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_T1_Saddles_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_T1_Weapons_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_T2_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_T2_Saddles_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_T2_Weapons_ASA, 1)
                )
        );

        SupplyCrate_Cave_QualityTier3 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T1_Armor_ASA, 0.6),
                        new Loop(LootItemSet_CaveDrop_T1_Saddles_ASA, 0.6),
                        new Loop(LootItemSet_CaveDrop_T1_Weapons_ASA, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Armor_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_T2_Saddles_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_T2_Weapons_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_T3_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_T3_Saddles_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_T3_Weapons_ASA, 1)
                )
        );

        SupplyCrate_Cave_QualityTier4 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T1_Armor_ASA, 0.4),
                        new Loop(LootItemSet_CaveDrop_T1_Saddles_ASA, 0.4),
                        new Loop(LootItemSet_CaveDrop_T1_Weapons_ASA, 0.4),
                        new Loop(LootItemSet_CaveDrop_T2_Armor_ASA, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Saddles_ASA, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Weapons_ASA, 0.6),
                        new Loop(LootItemSet_CaveDrop_T3_Armor_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_T3_Saddles_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_T3_Weapons_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_T4_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_T4_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_T4_Armor_ASA, 1)
                )
        );

        SupplyCrate_IceCaveTier1 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T1_Armor, 0.4),
                        new Loop(LootItemSet_CaveDrop_T1_Saddles, 0.4),
                        new Loop(LootItemSet_CaveDrop_T1_Weapons, 0.4),
                        new Loop(LootItemSet_CaveDrop_T2_Armor, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Saddles, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Weapons, 0.6),
                        new Loop(LootItemSet_CaveDrop_Ice_T1_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Ice_T1_Saddles_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Ice_T1_Weapons_ASA, 1)
                )
        );

        SupplyCrate_IceCaveTier2 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T2_Armor, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Saddles, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Weapons, 0.6),
                        new Loop(LootItemSet_CaveDrop_Ice_T1_Armor_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Ice_T1_Saddles_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Ice_T1_Weapons_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Ice_T2_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Ice_T2_Saddles_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Ice_T2_Weapons_ASA, 1)
                )
        );

        SupplyCrate_IceCaveTier3 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_Ice_T1_Armor_ASA, 0.4),
                        new Loop(LootItemSet_CaveDrop_Ice_T1_Saddles_ASA, 0.4),
                        new Loop(LootItemSet_CaveDrop_Ice_T1_Weapons_ASA, 0.4),
                        new Loop(LootItemSet_CaveDrop_Ice_T2_Armor_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Ice_T2_Saddles_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Ice_T2_Weapons_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Ice_T3_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Ice_T3_Saddles_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Ice_T3_Weapons_ASA, 1)
                )
        );


        SupplyCrate_SwampCaveTier1 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T1_Armor, 0.4),
                        new Loop(LootItemSet_CaveDrop_T1_Saddles, 0.4),
                        new Loop(LootItemSet_CaveDrop_T1_Weapons, 0.4),
                        new Loop(LootItemSet_CaveDrop_T2_Armor, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Saddles, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Weapons, 0.6),
                        new Loop(LootItemSet_CaveDrop_Swamp_T1_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Swamp_T1_Saddles_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Swamp_T1_Weapons_ASA, 1)
                )
        );

        SupplyCrate_SwampCaveTier2 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T2_Armor, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Saddles, 0.6),
                        new Loop(LootItemSet_CaveDrop_T2_Weapons, 0.6),
                        new Loop(LootItemSet_CaveDrop_Swamp_T1_Armor_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Swamp_T1_Saddles_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Swamp_T1_Weapons_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Swamp_T2_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Swamp_T2_Saddles_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Swamp_T2_Weapons_ASA, 1)
                )
        );

        SupplyCrate_SwampCaveTier3 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_Swamp_T1_Armor_ASA, 0.4),
                        new Loop(LootItemSet_CaveDrop_Swamp_T1_Saddles_ASA, 0.4),
                        new Loop(LootItemSet_CaveDrop_Swamp_T1_Weapons_ASA, 0.4),
                        new Loop(LootItemSet_CaveDrop_Swamp_T2_Armor_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Swamp_T2_Saddles_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Swamp_T2_Weapons_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Swamp_T3_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Swamp_T3_Saddles_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Swamp_T3_Weapons_ASA, 1)
                )
        );


        SupplyCrate_UnderwaterCaveTier1 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T4_Armor_ASA, 0.7),
                        new Loop(LootItemSet_CaveDrop_T4_Saddles_ASA, 0.7),
                        new Loop(LootItemSet_CaveDrop_T4_Weapons_ASA, 0.7),
                        new Loop(LootItemSet_CaveDrop_Underwater_T1_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Underwater_T1_Saddles_ASA, 1)
                )
        );


        SupplyCrate_UnderwaterCaveTier2 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T4_Armor_ASA, 0.5),
                        new Loop(LootItemSet_CaveDrop_T4_Saddles_ASA, 0.5),
                        new Loop(LootItemSet_CaveDrop_T4_Weapons_ASA, 0.5),
                        new Loop(LootItemSet_CaveDrop_Underwater_T1_Armor_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Underwater_T1_Saddles_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Underwater_T2_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Underwater_T2_Saddles_ASA, 1)
                )
        );


        SupplyCrate_UnderwaterCaveTier3 = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T4_Armor_ASA, 0.3),
                        new Loop(LootItemSet_CaveDrop_T4_Saddles_ASA, 0.3),
                        new Loop(LootItemSet_CaveDrop_T4_Weapons_ASA, 0.3),
                        new Loop(LootItemSet_CaveDrop_Underwater_T1_Armor_ASA, 0.6),
                        new Loop(LootItemSet_CaveDrop_Underwater_T1_Saddles_ASA, 0.6),
                        new Loop(LootItemSet_CaveDrop_Underwater_T2_Armor_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Underwater_T2_Saddles_ASA, 0.8),
                        new Loop(LootItemSet_CaveDrop_Underwater_T3_Armor_ASA, 1),
                        new Loop(LootItemSet_CaveDrop_Underwater_T3_Saddles_ASA, 1)
                )
        );

        SupplyCrate_OceanInstant = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_SupplyDrop_Ocean_ASA, 1)
                )
        );

        SupplyCreate_OceanInstant_High = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_SupplyDrop_Ocean_ASA, 1)
                )
        );


        SupplyCrate_Cave_QualityTier1_ScorchedEarth = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T1_Armor_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T1_Saddles_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T1_Weapons_ScorchedEarth, 1)
                )
        );


        SupplyCrate_Cave_QualityTier2_ScorchedEarth = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T1_Armor_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T1_Saddles_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T1_Weapons_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T2_Saddles_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T2_Weapons_ScorchedEarth, 1)
                )
        );


        SupplyCrate_Cave_QualityTier3_ScorchedEarth = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_CaveDrop_T1_Armor_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T1_Saddles_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T1_Weapons_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T2_Saddles_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T2_Weapons_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T3_Weapons_ScorchedEarth, 1),
                        new Loop(LootItemSet_CaveDrop_T3_Weapons_ScorchedEarth, 1)
                )
        );


        SupplyCrate_Cave_QualityTier3_TheCenter = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_SupplyDrop_Ocean_ASA, 1)
                )
        );


        SupplyCrate_Cave_QualityTier3_Ragnarok = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_SupplyDrop_Ocean_ASA, 1)
                )
        );


        SupplyCrate_Cave_QualityTier4_Ragnarok = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_SupplyDrop_Ocean_ASA, 1)
                )
        );

        SupplyCrate_Chest_Treasure_JacksonL = new SupplyCrate(
                CollectionUtil.newArrayList(
                        new Loop(LootItemSet_SupplyDrop_Ocean_ASA, 1)
                )
        );


        allSupplyCrate.put(SupplyCrate_Cave_QualityTier1, "SupplyCrate_Cave_QualityTier1");
        allSupplyCrate.put(SupplyCrate_Cave_QualityTier2, "SupplyCrate_Cave_QualityTier2");
        allSupplyCrate.put(SupplyCrate_Cave_QualityTier3, "SupplyCrate_Cave_QualityTier3");
        allSupplyCrate.put(SupplyCrate_Cave_QualityTier4, "SupplyCrate_Cave_QualityTier4");
        allSupplyCrate.put(SupplyCrate_IceCaveTier1, "SupplyCrate_IceCaveTier1");
        allSupplyCrate.put(SupplyCrate_IceCaveTier2, "SupplyCrate_IceCaveTier2");
        allSupplyCrate.put(SupplyCrate_IceCaveTier3, "SupplyCrate_IceCaveTier3");
        allSupplyCrate.put(SupplyCrate_SwampCaveTier1, "SupplyCrate_SwampCaveTier1");
        allSupplyCrate.put(SupplyCrate_SwampCaveTier2, "SupplyCrate_SwampCaveTier2");
        allSupplyCrate.put(SupplyCrate_SwampCaveTier3, "SupplyCrate_SwampCaveTier3");
        allSupplyCrate.put(SupplyCrate_UnderwaterCaveTier1, "SupplyCrate_UnderwaterCaveTier1");
        allSupplyCrate.put(SupplyCrate_UnderwaterCaveTier2, "SupplyCrate_UnderwaterCaveTier2");
        allSupplyCrate.put(SupplyCrate_UnderwaterCaveTier3, "SupplyCrate_UnderwaterCaveTier3");
        allSupplyCrate.put(SupplyCrate_OceanInstant, "SupplyCrate_OceanInstant");
        allSupplyCrate.put(SupplyCreate_OceanInstant_High, "SupplyCreate_OceanInstant_High");
        allSupplyCrate.put(SupplyCrate_Cave_QualityTier1_ScorchedEarth, "SupplyCrate_Cave_QualityTier1_ScorchedEarth(1.5-2.5)");
        allSupplyCrate.put(SupplyCrate_Cave_QualityTier2_ScorchedEarth, "SupplyCrate_Cave_QualityTier2_ScorchedEarth(2.4-3.5)");
        allSupplyCrate.put(SupplyCrate_Cave_QualityTier3_ScorchedEarth, "SupplyCrate_Cave_QualityTier3_ScorchedEarth(3.5-4.7)");

    }


    public static void main(String[] args) {

        ARALoop araLoop = new ARALoop();
        araLoop.init();

        Map<String, Map<String, Double>> mapList = new HashMap<>();

        araLoop.allSupplyCrate.forEach((k, v) -> {
            // 总权重
            double allWeight = 0;
            for (Loop loop : k.loops) {
                allWeight += loop.weight;
            }
            Map<String, Double> map = new HashMap<>();
            for (Loop loop : k.loops) {
                // 单个物品概率 = 组物品权重/总权重/组物品数量
                for (String lootItem : loop.list) {
                    // String chineseItemName = araLoop.all.get(lootItem);
                    if (map.containsKey(lootItem)) {
                        map.put(lootItem, map.get(lootItem) + (loop.weight / allWeight / loop.list.size()));
                    } else {
                        map.put(lootItem, (loop.weight / allWeight / loop.list.size()));
                    }
                }
            }
            System.out.println("===================================================" + v + "===================================================");
            Map<String, Double> sortedMap = map.entrySet().stream()
                    .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
            sortedMap.forEach((k1, v1) -> {
                System.out.printf("物品：%s(%s), 概率：%s%n", k1, araLoop.all.get(k1), v1);
            });
            System.out.println("===================================================" + v + "===================================================");
            System.out.println();

            mapList.put(v, sortedMap);
        });

        List<ThreeNode> threeNodes = new ArrayList<>();

        // 找到概率最高的宝箱
        String key = "大地";
        //  Map<String, Map<String, Double>> mapList = new HashMap<>();
        mapList.forEach((k, v) -> v.forEach((k1, v1) -> {
            if (Objects.nonNull(araLoop.all.get(k1)) && araLoop.all.get(k1).contains(key)) {
                threeNodes.add(new ThreeNode(k, k1, v1));
            }
        }));

        List<ThreeNode> collect = threeNodes.stream()
                .sorted((o1, o2) -> o2.value.compareTo(o1.value)).collect(Collectors.toList());

        collect.forEach(v -> System.out.printf("宝箱: %s,物品：%s(%s), 概率：%s%n", v.loopName, v.itemName, araLoop.all.get(v.itemName), v.value));
        System.out.println();
        System.out.println();
        System.out.println();
        Map<String, ThreeNode> threeNodeSet = new HashMap<>();
        collect.forEach((v) -> {
            String substring = v.getLoopName().substring(0, v.loopName.length() - 1);
            if (threeNodeSet.containsKey(substring)) {
                threeNodeSet.get(substring).setValue(threeNodeSet.get(substring).value + v.value);
            } else {
                ThreeNode threeNode = new ThreeNode(substring, v.itemName, v.value);
                threeNodeSet.put(substring, threeNode);
            }
        });

        // threeNodeSet.forEach((k, v) -> System.out.printf("宝箱: %s,物品：%s(%s), 概率：%s%n", v.loopName, v.itemName, araLoop.all.get(v.itemName), v.value));
    }
}
